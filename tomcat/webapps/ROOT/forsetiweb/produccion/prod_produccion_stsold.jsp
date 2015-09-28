<html>
<head>
<style>
/* CoolMenus 4 - default styles - do not edit */
.clCMAbs{position:absolute; visibility:hidden; left:0; top:0}
/* CoolMenus 4 - default styles - end */

/*Styles for level 0*/
.clLevel0,.clLevel0over{position:absolute; padding:2px; font-family:tahoma,arial,helvetica; font-size:15px; font-weight:bold}
.clLevel0{background-color:red; layer-background-color:#000099; color:white;}
.clLevel0over{background-color:gray; layer-background-color:#336699; color:Yellow; cursor:pointer; cursor:hand; }
.clLevel0border{position:absolute; visibility:hidden; background-color:#red; layer-background-color:red}

</style>
<script language="JavaScript1.2" src="../coolmenus4.js">
/*****************************************************************************
Copyright (c) 2001 Thomas Brattli (webmaster@dhtmlcentral.com)

DHTML coolMenus - Get it at coolmenus.dhtmlcentral.com
Version 4.0_beta
This script can be used freely as long as all copyright messages are
intact.

Extra info - Coolmenus reference/help - Extra links to help files **** 
CSS help: http://coolmenus.dhtmlcentral.com/projects/coolmenus/reference.asp?m=37
General: http://coolmenus.dhtmlcentral.com/reference.asp?m=35
Menu properties: http://coolmenus.dhtmlcentral.com/properties.asp?m=47
Level properties: http://coolmenus.dhtmlcentral.com/properties.asp?m=48
Background bar properties: http://coolmenus.dhtmlcentral.com/properties.asp?m=49
Item properties: http://coolmenus.dhtmlcentral.com/properties.asp?m=50
******************************************************************************/
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></head>
<body bgcolor="#000000">
<script>

/*** 
This is the menu creation code - place it right after you body tag
Feel free to add this to a stand-alone js file and link it to your page.
**/

//Menu object creation
oCMenu=new makeCM("oCMenu") //Making the menu object. Argument: menuname

oCMenu.frames = 0

//Menu properties   
oCMenu.pxBetween=10
oCMenu.fromLeft=0
oCMenu.fromTop=5  
oCMenu.cols=1 
oCMenu.menuPlacement="top"
                                                             
oCMenu.offlineRoot="file:///C|/Inetpub/wwwroot/dhtmlcentral/projects/coolmenus/" 
oCMenu.onlineRoot="" 
oCMenu.resizeCheck=1 
oCMenu.wait=1000 
oCMenu.fillImg="cm_fill.gif"
oCMenu.zIndex=0

//Background bar properties
oCMenu.useBar=0

//Level properties - ALL properties have to be spesified in level 0
oCMenu.level[0]=new cm_makeLevel() //Add this for each new level
oCMenu.level[0].width=20
oCMenu.level[0].height=20
oCMenu.level[0].regClass="clLevel0"
oCMenu.level[0].overClass="clLevel0over"
oCMenu.level[0].borderX=0
oCMenu.level[0].borderY=0
oCMenu.level[0].borderClass="clLevel0border"
oCMenu.level[0].offsetX=0
oCMenu.level[0].offsetY=0
oCMenu.level[0].rows=0
oCMenu.level[0].arrow=0
oCMenu.level[0].arrowWidth=0
oCMenu.level[0].arrowHeight=0
oCMenu.level[0].align="bottom"

/******************************************
Menu item creation:
myCoolMenu.makeMenu(name, parent_name, text, link, target, width, height, regImage, overImage, regClass, overClass , align, rows, nolink, onclick, onmouseover, onmouseout) 
*************************************/
oCMenu.makeMenu('top0','','&nbsp;T','/servlet/forseti.produccion.JProdProduccionCtrl?status=TODOS','cuerpo')
oCMenu.makeMenu('top1','','&nbsp;G','/servlet/forseti.produccion.JProdProduccionCtrl?status=GENERADOS','cuerpo')
oCMenu.makeMenu('top2','','&nbsp;E','/servlet/forseti.produccion.JProdProduccionCtrl?status=EMITIDOS','cuerpo')
oCMenu.makeMenu('top3','','&nbsp;C','/servlet/forseti.produccion.JProdProduccionCtrl?status=CANCELADOS','cuerpo')
oCMenu.makeMenu('top4','','&nbsp;P','/servlet/forseti.produccion.JProdProduccionCtrl?status=PENDIENTES','cuerpo')
oCMenu.makeMenu('top5','','&nbsp;R','/servlet/forseti.produccion.JProdProduccionCtrl?status=REVERTIDOS','cuerpo')

//Leave this line - it constructs the menu
oCMenu.construct()		
</script>

</body>
</html>
