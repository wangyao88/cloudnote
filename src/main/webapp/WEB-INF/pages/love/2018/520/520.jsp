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

<title>云端笔记-2018520表白日</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link rel="stylesheet" href="<%=basePath%>js/love/2018/520/css/default.css" />
<link rel="stylesheet" href="<%=basePath%>js/love/2018/520/css/transit.css" />

<script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath%>js/love/2018/520/js/garden.js" type="text/javascript"></script>
<script src="<%=basePath%>js/love/2018/520/js/functions.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=basePath%>js/love/2018/520/js/jquery.transit.js"></script>

</head>

<body>
	<div id='content'>
		<!-- 飘花 -->
		<div id="snowflake">
			<div id="mainDiv">
				<div id="content">
					<div id="code" style="font-size:20;">
						男孩名字 = <span class="keyword">王曜</span><br /> 
						女孩名字 = <span class="keyword">张秋雨</span><br /> 
						<span class="comments">//坠入爱河. </span><br /> 
						男孩深爱着女孩;<br /> 
						<span class="comments">//他们彼此相爱.</span><br /> 
						女孩也深爱着男孩;<br /> 
						<span class="comments">//随着时间的流逝.</span><br /> 
						男孩已经离不开女孩;<br /> 
						<span class="comments">//同时.</span><br /> 
						这个女孩也离不开男孩;<br /> 
						<span class="comments">// 无论是现在</span><br /> 
						<spanclass="comments">// 还是在遥远的未来.</span><br /> 
						男孩一直有个坚定的梦想;<br /> 
						<span class="comments">//男孩希望女孩一直幸福的陪在自己身边.</span><br /> <br> <br> 
						我想说:<br /> 
						亲爱的,我永远爱你;<br />
					</div>
					<div id="loveHeart">
						<canvas id="garden"></canvas>
						<div id="words">
							<div id="messages">
								亲爱的，这是我们相爱在一起的时光。
								<div id="elapseClock"></div>
							</div>
							<div id="loveu">
								爱你直到永永远远。<br />
								<div class="signature">- 王先生</div>
							</div>
						</div>
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
			}, 2000);
	
			timeElapse(together);
			setInterval(function() {
				timeElapse(together);
			}, 500);
	
			adjustCodePosition();
			$("#code").typewriter();
		}
	</script>
	<script type="text/javascript">
	$(function() {
	var path = "<%=basePath%>" + "js/love/2018/520";
        var snowflakeURl = [
            path+'./images/icon_petal_1.png',
            path+'/images/icon_petal_2.png',
            path+'/images/icon_petal_3.png',
            path+'/images/icon_petal_4.png',
            path+'/images/icon_petal_5.png',
            path+'/images/icon_petal_6.png',
            path+'/images/icon_petal_7.png',
            path+'/images/icon_petal_8.png'
        ]  
        var container = $("#content");
           visualWidth = container.width();
           visualHeight = container.height();
    　　//获取content的宽高
        function snowflake() {
            // 雪花容器
            var $flakeContainer = $('#snowflake');
    　　　　　　
            // 随机六张图
            function getImagesName() {
                return snowflakeURl[[Math.floor(Math.random() * 8)]];
            }
            // 创建一个雪花元素
            function createSnowBox() {
                var url = getImagesName();
                return $('<div class="snowbox" />').css({
                    'width': 25,
                    'height': 26,
                    'position': 'absolute',
                    'backgroundRepeat':'no-repeat',
                    'zIndex': 100000,
                    'top': '-41px',
                    'backgroundImage': 'url(' + url + ')'
                }).addClass('snowRoll');
            }
            // 开始飘花
            setInterval(function() {
                // 运动的轨迹
                var startPositionLeft = Math.random() * visualWidth - 100,
                    startOpacity    = 1,
                    endPositionTop  = visualHeight - 40,
                    endPositionLeft = startPositionLeft - 100 + Math.random() * 500,
                    duration        = visualHeight * 10 + Math.random() * 5000;
                // 随机透明度，不小于0.5
                var randomStart = Math.random();
                randomStart = randomStart < 0.5 ? startOpacity : randomStart;
                // 创建一个雪花
                var $flake = createSnowBox();
                // 设计起点位置
                $flake.css({
                    left: startPositionLeft,
                    opacity : randomStart
                });
                // 加入到容器
                $flakeContainer.append($flake);
                // 开始执行动画
                $flake.transition({
                    top: endPositionTop,
                    left: endPositionLeft,
                    opacity: 0.7
                }, duration, 'ease-out', function() {
                    $(this).remove() //结束后删除
                });
            }, 500);
        }
         snowflake()
    　　　//执行函数
    })
	</script>
</body>
</html>