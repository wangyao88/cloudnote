<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>曼妙云端云笔记-2018520表白日</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link rel="stylesheet"
	href="<%=basePath%>js/love/2018/520/css/default.css" />

<script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath%>js/love/2018/520/js/garden.js"
	type="text/javascript"></script>
<script src="<%=basePath%>js/love/2018/520/js/functions.js"
	type="text/javascript"></script>

</head>

<body>
	<div id="mainDiv"> <div id="content"> <div id="code">
	<span class="comments">/**</span><br />
			<span class="space"/><span class="comments">*2013—02-14,</span><br />
			<span class="space"/><span class="comments">*2013-02-28.</span><br />
			<span class="space"/><span class="comments">*/</span><br />
			Boy name = <span class="keyword">Mr</span> LI<br />
			Girl name = <span class="keyword">Mrs</span> ZHANG<br />
			<span class="comments">// Fall in love river. </span><br />
			The boy love the girl;<br />
			<span class="comments">// They love each other.</span><br />
			The girl loved the boy;<br />
			<span class="comments">// AS time goes on.</span><br />
			The boy can not be separated the girl;<br />
			<span class="comments">// At the same time.</span><br />
			The girl can not be separated the boy;<br />
			<span class="comments">// Both wind and snow all over the sky.</span><br />
			<span class="comments">// Whether on foot or 5 kilometers.</span><br />
			<span class="keyword">The boy</span> very <span class="keyword">happy</span>;<br />
			<span class="keyword">The girl</span> is also very <span class="keyword">happy</span>;<br />
			<span class="placeholder"/><span class="comments">// Whether it is right now</span><br />
			<span class="placeholder"/><span class="comments">// Still in the distant future.</span><br />
			<span class="placeholder"/>The boy has but one dream;<br />
			<span class="comments">// The boy wants the girl could well have been happy.</span><br />
			<br>
			<br>
			I want to say:<br />
			Baby, I love you forever;<br />
		</div>
		<div id="loveHeart">
			<canvas id="garden"></canvas>
			<div id="words">
				<div id="messages">
					亲爱的，这是我们相爱在一起的时光。
					<div id="elapseClock"></div>
				</div>
				<div id="loveu">
					爱你直到永永远远。<br/>
					<div class="signature">- 王先生</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	var offsetX = $("#loveHeart").width() / 2;
	var offsetY = $("#loveHeart").height() / 2 - 55;
	var together = new Date();
	together.setFullYear(2015, 4, 29);
	together.setHours(20);
	together.setMinutes(0);
	together.setSeconds(0);
	together.setMilliseconds(0);

	if (!document.createElement('canvas').getContext) {
		var msg = document.createElement("div");
		msg.id = "errorMsg";
		msg.innerHTML = "Your browser doesn't support HTML5!<br/>Recommend use Chrome 14+/IE 9+/Firefox 7+/Safari 4+";
		document.body.appendChild(msg);
		$("#code").css("display", "none")
		$("#copyright").css("position", "absolute");
		$("#copyright").css("bottom", "10px");
		document.execCommand("stop");
	} else {
		setTimeout(function() {
			startHeartAnimation();
		}, 5000);

		timeElapse(together);
		setInterval(function() {
			timeElapse(together);
		}, 500);

		adjustCodePosition();
		$("#code").typewriter();
	}
</script>

</body>
</html>