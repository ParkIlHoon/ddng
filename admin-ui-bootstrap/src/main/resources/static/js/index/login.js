//난수 생성 함수
function generateRandom (min, max)
{
    var ranNum = Math.floor(Math.random()*(max-min+1)) + min;
    return ranNum;
}

////뱀게임///
var score = 0;
var LR = 0; // 좌우 방향
var TB = 1; // 위아래 방향
var mapSize = 21; //map size
var gameInterval;
//snake
var snake = new Array();
//food
var food = new Array();

// init map
function initMap()
{
    var tableCode = '';
    for(var i=0; i<mapSize; i++)
    {
        tableCode += '<tr>';
        var rowCode = '';
        for(var j=0; j<mapSize; j++)
        {
            rowCode += '<td id="block'+i+'_'+j+'"></td>';
        }
        tableCode += rowCode + '</tr>';
        $('#snakeTable').html(tableCode);
    }
}

// init snake
function initSnake()
{
    snake = [];
    snake.push([0,1]);
    drawSnake();
}

//뱀 그리기
function drawSnake()
{
    var state = '';
    $('#snakeTable td').removeClass('snake');
    for(var i=0;i<snake.length;i++) {
        $('#block'+snake[i][0]+'_'+snake[i][1]).addClass('snake');
        //먹이 먹었을 때
        if($('#block'+snake[i][0]+'_'+snake[i][1]).hasClass('food')){
            score++; // 점수 증가
            $('#score').text(score); //점수 반영
            food.pop(); // 먹이 제거
            initFood(); // 새로운 먹이 추가
            //뱀 꼬리 늘리기
            state = 'eat';
        }
    }
    return state;
}

// 먹이 초기화
function initFood(){
    var x;
    var y;

    do{
        x = generateRandom(0,mapSize-1);
        y = generateRandom(0,mapSize-1);
    }while($('#block'+x+'_'+y).hasClass('snake')); // 뱀이랑 겹치면 다시

    food = [];
    food.push([x, y]);
    drawFood();
}

//먹이 그리기
function drawFood() {
    $('#snakeTable td').removeClass('food');
    for(var i=0;i<food.length;i++) {
        $('#block'+food[i][0]+'_'+food[i][1]).addClass('food');
    }
}


// move
function move() {
    var head = new Array();
    head[0] = snake[0][0];
    head[1] = snake[0][1];

    // 벽을 만난건지 체크
    var tmp = head[0]+1*TB;
    if(tmp >= 0 && tmp < mapSize) {
        head[0] = tmp;
    }else {
        end();
        initAll();
        return;
    }

    tmp = head[1]+1*LR;
    if(tmp >= 0 && tmp < mapSize) {
        head[1] = tmp;
    }else {
        end();
        initAll();
        return;
    }

    // 몸통을 만난건지 체크
    if($('#block'+head[0]+'_'+head[1]).hasClass('snake')){
        end();
        initAll();
        return;
    }


    snake.unshift(head);

    if(drawSnake() != 'eat') { //먹은게 아니면
        snake.pop(); //꼬리 연장 X
    }
}


function left() {
    if(TB == 0) return; // 반대방향으로 방향전환 불가
    LR = -1;
    TB = 0;
}
function right() {
    if(TB == 0) return; // 반대방향으로 방향전환 불가
    LR = 1;
    TB = 0;
}
function up() {
    if(LR == 0) return; // 반대방향으로 방향전환 불가
    LR = 0;
    TB = -1;
}
function down() {
    if(LR == 0) return; // 반대방향으로 방향전환 불가
    LR = 0;
    TB = 1;
}


$(document).on('click', '#startBtn', function(){
    end();
    start();
});

$(document).on('keydown', 'body', function(event){
    if(event.key == 'ArrowUp') {
        up();
    }else if(event.key == 'ArrowDown') {
        down();
    }else if(event.key == 'ArrowLeft') {
        left();
    }else if(event.key == 'ArrowRight') {
        right();
    }
});

function initAll() {
    score = 0; // 점수 초기화
    initMap(); // 맵 초기화
    initFood(); // 먹이 초기화
    initSnake(); // init snake
    LR = 0; // 좌우 방향 초기화
    TB = 1; // 위아래 방향 초기화
}

function start(){
    $("#snakeTable").show();
    gameInterval = setInterval(move, 70);
    $("#snakeTable").focus();
}

function end() {
    clearInterval(gameInterval);
    $("#snakeTable").hide();
}

$(document).ready(function(){
    $("#snakeTable").hide();
    initAll();
});

// Source from https://dororongju.tistory.com/101