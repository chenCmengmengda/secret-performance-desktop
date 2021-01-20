/****************************************************************
 *																*		
 * 						      代码库							*
 *                        www.dmaku.com							*
 *       		  努力创建完善、持续更新插件以及模板			*
 * 																*
****************************************************************/
$(function(){

  var canvas = document.querySelector('canvas'),
	  ctx = canvas.getContext('2d')
  canvas.width = window.innerWidth;
  canvas.height = window.innerHeight;
  ctx.lineWidth = .3;
  ctx.strokeStyle = (new Color(150)).style;
  var dotsNomber = 400;
  if(canvas.width < 1400){
	  dotsNomber = 200;
  }

  var mousePosition = {
	x: 30 * canvas.width / 100,
	y: 30 * canvas.height / 100
  };

  var dots = {
	nb: dotsNomber,
	distance: 100,
	d_radius: 150,
	array: []
  };


  function colorValue(min) {
	return Math.floor(Math.random() * 255 + min);
  }

  function createColorStyle(r,g,b) {
	return 'rgba(' + r + ',' + g + ',' + b + ', 0.8)';
  }

  function mixComponents(comp1, weight1, comp2, weight2) {
	return (comp1 * weight1 + comp2 * weight2) / (weight1 + weight2);
  }

  function averageColorStyles(dot1, dot2) {
	var color1 = dot1.color,
		color2 = dot2.color;

	var r = mixComponents(color1.r, dot1.radius, color2.r, dot2.radius),
		g = mixComponents(color1.g, dot1.radius, color2.g, dot2.radius),
		b = mixComponents(color1.b, dot1.radius, color2.b, dot2.radius);
	return createColorStyle(Math.floor(r), Math.floor(g), Math.floor(b));
  }

  function Color(min) {
	min = min || 0;
	this.r = colorValue(min);
	this.g = colorValue(min);
	this.b = colorValue(min);
	this.style = createColorStyle(this.r, this.g, this.b);
  }

  function Dot(){
	this.x = Math.random() * canvas.width;
	this.y = Math.random() * canvas.height;

	this.vx = -.5 + Math.random();
	this.vy = -.5 + Math.random();

	this.radius = Math.random() * 2;

	this.color = new Color();
	//console.log(this);
  }

  Dot.prototype = {
	draw: function(){
	  ctx.beginPath();
	  ctx.fillStyle = this.color.style;
	  ctx.arc(this.x, this.y, this.radius, 0, Math.PI * 2, false);
	  ctx.fill();
	}
  };

  function createDots(){
	for(i = 0; i < dots.nb; i++){
	  dots.array.push(new Dot());
	}
  }

  function moveDots() {
	for(i = 0; i < dots.nb; i++){

	  var dot = dots.array[i];

	  if(dot.y < 0 || dot.y > canvas.height){
		dot.vx = dot.vx;
		dot.vy = - dot.vy;
	  }
	  else if(dot.x < 0 || dot.x > canvas.width){
		dot.vx = - dot.vx;
		dot.vy = dot.vy;
	  }
	  dot.x += dot.vx;
	  dot.y += dot.vy;
	}
  }

  function connectDots() {
	for(i = 0; i < dots.nb; i++){
	  for(j = 0; j < dots.nb; j++){
		i_dot = dots.array[i];
		j_dot = dots.array[j];

		if((i_dot.x - j_dot.x) < dots.distance && (i_dot.y - j_dot.y) < dots.distance && (i_dot.x - j_dot.x) > - dots.distance && (i_dot.y - j_dot.y) > - dots.distance){
		  if((i_dot.x - mousePosition.x) < dots.d_radius && (i_dot.y - mousePosition.y) < dots.d_radius && (i_dot.x - mousePosition.x) > - dots.d_radius && (i_dot.y - mousePosition.y) > - dots.d_radius){
			ctx.beginPath();
			ctx.strokeStyle = averageColorStyles(i_dot, j_dot);
			ctx.moveTo(i_dot.x, i_dot.y);
			ctx.lineTo(j_dot.x, j_dot.y);
			ctx.stroke();
			ctx.closePath();
		  }
		}
	  }
	}
  }

  function drawDots() {
	for(i = 0; i < dots.nb; i++){
	  var dot = dots.array[i];
	  dot.draw();
	}
  }

  function animateDots() {
	ctx.clearRect(0, 0, canvas.width, canvas.height);
	moveDots();
	connectDots();
	drawDots();

	requestAnimationFrame(animateDots);
  }

  $('canvas').on('mousemove', function(e){
	mousePosition.x = e.pageX;
	mousePosition.y = e.pageY;
  });

  $('canvas').on('mouseleave', function(e){
	mousePosition.x = canvas.width / 3;
	mousePosition.y = canvas.height / 3;
  });

  createDots();
  requestAnimationFrame(animateDots);

});console.log("\u002f\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u000d\u000a\u0020\u002a\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u002a\u0009\u0009\u000d\u000a\u0020\u002a\u0020\u0009\u0009\u0009\u0009\u0009\u0009\u0020\u0020\u0020\u0020\u0020\u0020\u4ee3\u7801\u5e93\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u002a\u000d\u000a\u0020\u002a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0077\u0077\u0077\u002e\u0064\u006d\u0061\u006b\u0075\u002e\u0063\u006f\u006d\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u002a\u000d\u000a\u0020\u002a\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0009\u0009\u0020\u0020\u52aa\u529b\u521b\u5efa\u5b8c\u5584\u3001\u6301\u7eed\u66f4\u65b0\u63d2\u4ef6\u4ee5\u53ca\u6a21\u677f\u0009\u0009\u0009\u002a\u000d\u000a\u0020\u002a\u0020\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u002a\u000d\u000a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002a\u002f");
