// Node objectfunction Node(id, pid, name, url, title, target, icon, iconOpen, open, defaultChoice) {	this.id = id; // 本节点ID 	this.pid = pid; // 父节点ID 	this.name = name; // 本节点名称	this.url = url; // 本节点的URL 	this.title = title; // 鼠标悬浮于节点上的提示信息	this.target = target; // 在哪个框架上打开连接	this.icon = icon; // 节点图标	this.iconOpen = iconOpen; // 节点展开后的图标	this.defaultChoice = defaultChoice; /* 设置是否默认选中*/	this._io = open || false; // 节点是否已打开的标记	this._is = false; // is = is selected, 标记该节点是否是被选中的节点 	this._ls = false; // ls = last slibling, 是否最后一个节点	this._hc = false; // hc = has children, 标记是否具有子节点	this._ai = 0; // ai = array index, 节点在数组中的下标	this._p; // 节点的父节点};// Tree objectfunction dTree(objName) {	this.config = {		target : null,		folderLinks : true,   /* 文件夹图表上是否具有连接 */		useSelection : true,  /* 选中部分是否高亮显示 */		useCookies : true,  /* 是否使用cookie */		useLines : true,  /* 显示连接线 */		useIcons : true, /* 显示图标 */		useStatusText : false,  /* 在状态栏显示提示信息 */		closeSameLevel : false,  /* 关闭同级其他节点 */		inOrder : false, 		useRadio : false, /* 设置是否有添加单选按钮 */		useCheckbox : false, /* 设置是否使用复选框 */		useCheckboxState : false /* 选用复选框，true普通选择只选中当前选中的 */	}	this.icon = { /* 指定各个图标 */		root : '../resources/img/base.gif', /* 根节点图标 */		folder : '../resources/img/folder.gif',  /* 未展开节点图标 */		folderOpen : '../resources/img/folderopen.gif',  /* 展开后节点图标 */		node : '../resources/img/page.gif',  /* 叶子节点(无节点的节点)图标 */		empty : '../resources/img/empty.gif',  /* 空白图标 */		line : '../resources/img/line.gif',  /* 竖向连接线图标 */		join : '../resources/img/join.gif',  /* 兼具水平、竖向连接线的图标 */		joinBottom : '../resources/img/joinbottom.gif', /* 底端连接线图标 */		plus : '../resources/img/plus.gif',  /* 带连接线的+图标 */		plusBottom : '../resources/img/plusbottom.gif',  /* 带连接线的底端+图标 */		minus : '../resources/img/minus.gif',  /* 带连接线的-图标 */		minusBottom : '../resources/img/minusbottom.gif',  /* 带连接线的底端-图标 */		nlPlus : '../resources/img/nolines_plus.gif', /* 不带连接线的+图标 */		nlMinus : '../resources/img/nolines_minus.gif'  /* 不带连接线的-图标 */	};	this.obj = objName; /* 树对象名称 */	this.aNodes = [];  /* 节点数组 */	this.aIndent = []; /* 缩进数组 */	this.root = new Node(-1);  /* 根节点 */	this.selectedNode = null; /* 选定节点 */	this.selectedFound = false; /* 是否已经选定节点 */	this.completed = false;  /* 结束标志 */};// Adds a new node to the node array// 添加节点到节点数组dTree.prototype.add = function(id, pid, name, url, title, target, icon,	iconOpen, open, defaultChoice) {	this.aNodes[this.aNodes.length] = new Node(id, pid, name, url, title, target, icon, iconOpen, open, defaultChoice);};// Open/close all nodesdTree.prototype.openAll = function() {	this.oAll(true);};dTree.prototype.closeAll = function() {	this.oAll(false);};// Outputs the tree to the page// 生成tree的html字符串dTree.prototype.toString = function() {	var str = '<div class="dtree">\n';	if (document.getElementById) {		if (this.config.useCookies)			this.selectedNode = this.getSelected();		str += this.addNode(this.root);	} else		str += 'Browser not supported.';	str += '</div>';	if (!this.selectedFound)		this.selectedNode = null;	this.completed = true;	return str;};// Creates the tree structure// 生成节点及其子节点的html字符串dTree.prototype.addNode = function(pNode) {	var str = '';	var n = 0;	// 默认在整个数组中搜索子节点	if (this.config.inOrder)		n = pNode._ai;	// 遍历节点数组 	for (n; n < this.aNodes.length; n++) {		// 只处理直接下级节点		if (this.aNodes[n].pid == pNode.id) {			// 临时变量			var cn = this.aNodes[n];			// 设置节点的父节点属性			cn._p = pNode;			// 设置节点的数组索引属性			cn._ai = n;			// 设置节点包含子节点标识_hc和同级最后节点标识_ls			this.setCS(cn);			// 设置节点target 属性			if (!cn.target && this.config.target)				cn.target = this.config.target;			// 判断一个包含子节点的节点在Cookie中是否是展开状态			if (cn._hc && !cn._io && this.config.useCookies)				cn._io = this.isOpen(cn.id);			// 判断是否允许包含子节点的节点带有超链接地址			if (!this.config.folderLinks && cn._hc)				cn.url = null;			// 判断节点是否被选中			if (this.config.useSelection && cn.id == this.selectedNode					&& !this.selectedFound) {				// 初始化节点选中标志				cn._is = true;				// 从这里开始this.selectedNode值由id变为_ai(节点数组索引)				this.selectedNode = n;				// 初始化tree的选中标志				this.selectedFound = true;			}			// 判断本级最后一个节点，结束循环			str += this.node(cn, n);			if (cn._ls)				break;		}	}	return str;};/** * Creates the node icon, url and text<br> * 生成节点的html字符串<br> * @param node 节点对象; * @param nodeId 节点在节点数组中的索引值; */dTree.prototype.node = function(node, nodeId) {	// 节点前的线条或空白图标	var str = '<div class="dTreeNode">' + this.indent(node, nodeId);	if (this.config.useIcons) {		// 根据节点类型和状态确定节点的默认图标		if (!node.icon)			node.icon = (this.root.id == node.pid) ? this.icon.root					: ((node._hc) ? this.icon.folder : this.icon.node);		if (!node.iconOpen)			node.iconOpen = (node._hc) ? this.icon.folderOpen : this.icon.node;		if (this.root.id == node.pid) {			node.icon = this.icon.root;			node.iconOpen = this.icon.root;		}		str += '<img id="i' + this.obj + nodeId + '" src="'				+ ((node._io) ? node.iconOpen : node.icon) + '" alt="" />';		// 添加单选按钮radio ,在调用页面把第一个节点id设置为0，该节点不添加单选按钮		// 此处的getRadioSelected方法具体实现在调用树的页面		if (this.config.useRadio && node.id != 0) {			str += '<input type="radio" name="chk" id="'+ nodeId					+ '" onclick="getRadioSelected(' + nodeId + ')" value="' + node.id + '"/>';		}		// 添加复选框，在调用页面把第一个节点id设置为0，该节点不添加复选框。此处的方法checkStatus在本JS内部实现		if (this.config.useCheckbox == true && node.id != 0) {			var name = node.name;			if (node.icon == '../resources/img/word.gif') {				name += ".word";			} else if (node.icon == '../resources/img/excel.gif') {				name += ".xls";			}			str += '<input type="checkbox" name="check" class="' + node.pid					+ '" value="' + node.id + '"" id="' + node.id + '" ';			if (!this.config.useCheckboxState) {				str += 'onclick="javascript:' + this.obj + '.checkStatus(' + node.id + ', this)"';			}			// 判断是否默认选中			if (node.defaultChoice) {				str += ' checked="checked"';			}			str += ' />'		}	}	// 节点文本及动作方法(带超链接、不带超链接)	if (node.url) {		str += '<a id="s'				+ this.obj				+ nodeId				+ '" class="'				+ ((this.config.useSelection) ? ((node._is ? 'nodeSel' : 'node'))						: 'node') + '" href="' + node.url + '"';		if (node.title)			str += ' title="' + node.title + '"';		if (node.target)			str += ' target="' + node.target + '"';		if (this.config.useStatusText)			str += ' onmouseover="window.status=\'' + node.name + '\';return true;" onmouseout="window.status=\'\';return true;" ';		if (this.config.useSelection				&& ((node._hc && this.config.folderLinks) || !node._hc))			str += ' onclick="javascript: ' + this.obj + '.s(' + nodeId + ');"';		str += '>';	}	else if ((!this.config.folderLinks || !node.url) && node._hc			&& node.pid != this.root.id)		str += '<a href="javascript: ' + this.obj + '.o(' + nodeId				+ ');" class="node">';	str += node.name;	if (node.url || ((!this.config.folderLinks || !node.url) && node._hc))		str += '</a>';	str += '</div>';	// --------- 以上是节点面板 --------	// --------- 以下是包含子节点的面板 --------	if (node._hc) {		str += '<div id="d' + this.obj + nodeId				+ '" class="clip" style="display:'				+ ((this.root.id == node.pid || node._io) ? 'block' : 'none')				+ ';">';		str += this.addNode(node);		str += '</div>';	}	this.aIndent.pop();	return str;};/** * Adds the empty and line icons <br> * 根据当前节点到次级根节点的所有父节点是否是同级最后一个节点的属性<br> * 确定节点前面显示图标的数量和种类<br> * @param node 节点对象; * @param nodeId 节点在节点数组中的索引值; */dTree.prototype.indent = function(node, nodeId) {	var str = '';	if (this.root.id != node.pid) {		for ( var n = 0; n < this.aIndent.length; n++)			str += '<img src="' + ((this.aIndent[n] == 1 && this.config.useLines) ? this.icon.line					: this.icon.empty) + '" alt="" />';		(node._ls) ? this.aIndent.push(0) : this.aIndent.push(1);		if (node._hc) {			str += '<a href="javascript: ' + this.obj + '.o(' + nodeId					+ ');"><img id="j' + this.obj + nodeId + '" src="';			if (!this.config.useLines)				str += (node._io) ? this.icon.nlMinus : this.icon.nlPlus;			else				str += ((node._io) ? ((node._ls && this.config.useLines) ? this.icon.minusBottom						: this.icon.minus)						: ((node._ls && this.config.useLines) ? this.icon.plusBottom								: this.icon.plus));			str += '" alt="" /></a>';		} else			str += '<img src="' + ((this.config.useLines) ? ((node._ls) ? this.icon.joinBottom					: this.icon.join)					: this.icon.empty) + '" alt="" />';	}	return str;};/** * Checks if a node has any children and if it is the last sibling<br> * 设置节点包含子节点标识_hc和同级最后节点标识_ls<br> */dTree.prototype.setCS = function(node) {	var lastId;	for ( var n = 0; n < this.aNodes.length; n++) {		if (this.aNodes[n].pid == node.id)			node._hc = true;		if (this.aNodes[n].pid == node.pid)			lastId = this.aNodes[n].id;	}	if (lastId == node.id)		node._ls = true;};/** * Returns the selected node<br> * 从Cookie中取得被选中节点在节点数组中的索引<br> */dTree.prototype.getSelected = function() {	var sn = this.getCookie('cs' + this.obj);	return (sn) ? sn : null;};/** * Highlights the selected node<br> * 使选中的节点高亮显示<br> * @param id 节点在节点数组中的索引值; */dTree.prototype.s = function(id) {	// 判断是否允许选中节点	if (!this.config.useSelection)		return;	// 根据索引值从节点数组中取出节点对象	var cn = this.aNodes[id];	// 判断包含子节点的节点是否允许选中	if (cn._hc && !this.config.folderLinks)		return;	// 交换新旧节点的选中状态，改变css	if (this.selectedNode != id) {		if (this.selectedNode || this.selectedNode == 0) {			eOld = document.getElementById("s" + this.obj + this.selectedNode);			eOld.className = "node";		}		eNew = document.getElementById("s" + this.obj + id);		eNew.className = "nodeSel";		this.selectedNode = id;		if (this.config.useCookies)			this.setCookie('cs' + this.obj, cn.id);	}};// Toggle Open or closedTree.prototype.o = function(id) {	var cn = this.aNodes[id];	this.nodeStatus(!cn._io, id, cn._ls);	cn._io = !cn._io;	if (this.config.closeSameLevel)		this.closeLevel(cn);	if (this.config.useCookies)		this.updateCookie();};// Open or close all nodesdTree.prototype.oAll = function(status) {	for ( var n = 0; n < this.aNodes.length; n++) {		if (this.aNodes[n]._hc && this.aNodes[n].pid != this.root.id) {			this.nodeStatus(status, n, this.aNodes[n]._ls)			this.aNodes[n]._io = status;		}	}	if (this.config.useCookies)		this.updateCookie();};// Opens the tree to a specific nodedTree.prototype.openTo = function(nId, bSelect, bFirst) {	if (!bFirst) {		for ( var n = 0; n < this.aNodes.length; n++) {			if (this.aNodes[n].id == nId) {				nId = n;				break;			}		}	}	var cn = this.aNodes[nId];	if (cn.pid == this.root.id || !cn._p)		return;	cn._io = true;	cn._is = bSelect;	if (this.completed && cn._hc)		this.nodeStatus(true, cn._ai, cn._ls);	if (this.completed && bSelect)		this.s(cn._ai);	else if (bSelect)		this._sn = cn._ai;	this.openTo(cn._p._ai, false, true);};// Closes all nodes on the same level as certain nodedTree.prototype.closeLevel = function(node) {	for ( var n = 0; n < this.aNodes.length; n++) {		if (this.aNodes[n].pid == node.pid && this.aNodes[n].id != node.id				&& this.aNodes[n]._hc) {			this.nodeStatus(false, n, this.aNodes[n]._ls);			this.aNodes[n]._io = false;			this.closeAllChildren(this.aNodes[n]);		}	}}// Closes all children of a nodedTree.prototype.closeAllChildren = function(node) {	for ( var n = 0; n < this.aNodes.length; n++) {		if (this.aNodes[n].pid == node.id && this.aNodes[n]._hc) {			if (this.aNodes[n]._io)				this.nodeStatus(false, n, this.aNodes[n]._ls);			this.aNodes[n]._io = false;			this.closeAllChildren(this.aNodes[n]);		}	}}// Change the status of a node(open or closed)dTree.prototype.nodeStatus = function(status, id, bottom) {	eDiv = document.getElementById('d' + this.obj + id);	eJoin = document.getElementById('j' + this.obj + id);	if (this.config.useIcons) {		eIcon = document.getElementById('i' + this.obj + id);		eIcon.src = (status) ? this.aNodes[id].iconOpen : this.aNodes[id].icon;	}	eJoin.src = (this.config.useLines) ?	((status) ? ((bottom) ? this.icon.minusBottom : this.icon.minus)			: ((bottom) ? this.icon.plusBottom : this.icon.plus)) :	((status) ? this.icon.nlMinus : this.icon.nlPlus);	eDiv.style.display = (status) ? 'block' : 'none';};// [Cookie] Clears a cookiedTree.prototype.clearCookie = function() {	var now = new Date();	var yesterday = new Date(now.getTime() - 1000 * 60 * 60 * 24);	this.setCookie('co' + this.obj, 'cookieValue', yesterday);	this.setCookie('cs' + this.obj, 'cookieValue', yesterday);};// [Cookie] Sets value in a cookiedTree.prototype.setCookie = function(cookieName, cookieValue, expires, path,		domain, secure) {	document.cookie =	escape(cookieName) + '=' + escape(cookieValue)	+ (expires ? '; expires=' + expires.toGMTString() : '')	+ (path ? '; path=' + path : '')	+ (domain ? '; domain=' + domain : '')	+ (secure ? '; secure' : '');};// [Cookie] Gets a value from a cookiedTree.prototype.getCookie = function(cookieName) {	var cookieValue = '';	var posName = document.cookie.indexOf(escape(cookieName) + '=');	if (posName != -1) {		var posValue = posName + (escape(cookieName) + '=').length;		var endPos = document.cookie.indexOf(';', posValue);		if (endPos != -1)			cookieValue = unescape(document.cookie.substring(posValue, endPos));		else			cookieValue = unescape(document.cookie.substring(posValue));	}	return (cookieValue);};// [Cookie] Returns ids of open nodes as a stringdTree.prototype.updateCookie = function() {	var str = '';	for ( var n = 0; n < this.aNodes.length; n++) {		if (this.aNodes[n]._io && this.aNodes[n].pid != this.root.id) {			if (str)				str += '.';			str += this.aNodes[n].id;		}	}	this.setCookie('co' + this.obj, str);};// [Cookie] Checks if a node id is in a cookiedTree.prototype.isOpen = function(id) {	var aOpen = this.getCookie('co' + this.obj).split('.');	for ( var n = 0; n < aOpen.length; n++)		if (aOpen[n] == id)			return true;	return false;};// If Push and pop is not implemented by the browserif (!Array.prototype.push) {	Array.prototype.push = function array_push() {		for ( var i = 0; i < arguments.length; i++)			this[this.length] = arguments[i];		return this.length;	}};if (!Array.prototype.pop) {	Array.prototype.pop = function array_pop() {		lastElement = this[this.length - 1];		this.length = Math.max(this.length - 1, 0);		return lastElement;	}};dTree.prototype.checkStatus = function (no, chkBox) {	checkPar(chkBox);//当子目录选中时,父目录也选中	var chks = document.getElementsByName("check");//得到所有input	var b = chks.length;	for ( var i = 0; i < b; i++) {		if (chks[i].className == no) {//ID等于传进父目录ID时			chks[i].checked = chkBox.checked;//保持选中状态和父目录一致			this.checkStatus(chks[i].value, chks[i]);//递归保持所有的子目录选中		}	}};function checkPar(chkBox) {	// 判断本单击为选中,并且不是根目录,则选中父目录	if (chkBox.name.toLowerCase() == 'check' && chkBox.checked 	&& chkBox.className != 0) {		//得到父目录对象		var chkObject = document.getElementById(chkBox.className);		chkObject.checked = true;		checkPar(chkObject);	}};