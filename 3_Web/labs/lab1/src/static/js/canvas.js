const canvas = document.getElementById('canvas');
const ctx = canvas.getContext('2d');

const width = canvas.width;
const height = canvas.height;
const R = 100;
const centerX = width / 2;
const centerY = height / 2;

ctx.fillStyle = 'rgba(49, 152, 254, 0.2)';

ctx.beginPath();
ctx.rect(centerX, centerY, R, R / 2);
ctx.fill();

ctx.beginPath();
ctx.moveTo(centerX, centerY);
ctx.arc(centerX, centerY, R, 0, - Math.PI / 2, true);
ctx.lineTo(centerX, centerY);
ctx.fill();

ctx.beginPath();
ctx.moveTo(centerX, centerY);
ctx.lineTo(centerX - R / 2, centerY);
ctx.lineTo(centerX, centerY - R / 2);
ctx.closePath();
ctx.fill();

ctx.beginPath();
ctx.moveTo(centerX, 0); 
ctx.lineTo(centerX, height);
ctx.moveTo(0, centerY);
ctx.lineTo(width, centerY);
ctx.strokeStyle = "white";
ctx.stroke();

ctx.font = "10px monospace";

ctx.strokeText("0", centerX + 6, centerY - 6);
ctx.strokeText("R/2", centerX + R / 2 - 6, centerY - 6);
ctx.strokeText("R", centerX + R - 6, centerY - 6);

ctx.strokeText("-R/2", centerX - R / 2 - 6, centerY - 6);
ctx.strokeText("-R", centerX - R - 6, centerY - 6);

ctx.strokeText("R/2", centerX + 6, centerY - R / 2 + 6);
ctx.strokeText("R", centerX + 6, centerY - R / 2 + 6);

ctx.strokeText("-R/2", centerX + 6, centerY + R / 2 + 6);
ctx.strokeText("-R", centerX + 6, centerY + R + 6);