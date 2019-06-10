var express = require('express');
var router = express.Router();
var mysql = require('../public/javascripts/mysql.js');
var nodemailer = require('nodemailer');


/* GET home page. */
router.get('/', function(req, res, next) {
  var query = 'DELETE FROM `sign`.`customer` WHERE (`email` = \'wlsshddls@naver.com\') and (`id` = \'EbbunHorang\');';
  console.log(query); // 쿼리문 오류를 알기위한 출력
  mysql.select(query, function(rows){
    for(i=0; ; ){
      if(rows[i]==null){
        break;
      }
      console.log(rows[i]);
      i++;
    }
  });
  res.render('index', { title: 'Express' });
});

router.post('/test1', function(req, res, next){

  console.log("----------------------------");
  console.log('router min.js /sign post');
  console.log("----------------------------");
  var test = req.body.test1;
  console.log(test);

  res.json({"result":"ok"})
});

// 랜덤 (문자+ 숫자) 난수 생성
function randNum(){
  var ALPHA = ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9'];
  var randomStr='';
  for(var i=0; i<8; i++){
   var randTnum = Math.floor(Math.random()*ALPHA.length);
   randomStr += ALPHA[randTnum];
  }
  return randomStr;
 }

 //이메일 발송 함수
router.post('/sendEmail', function(req, res, next){
  console.log("----------------------------");
  console.log('router min.js /sendEmail post');
  console.log("----------------------------");

  var userEmail = req.body.userEmail;
  
  var randNumber = randNum();
  console.log(userEmail);
  console.log(randNumber);
  
  var pw = "pw를 선언을 해야지이이이ㅠㅠㅠㅠㅠㄴ";

  var transporter = nodemailer.createTransport({
    service: 'gmail',
    auth:{
      user: 'ung546834@gmail.com',  //gmail 계정 아이디를 입력
      pass: 'dbwjd12!'          //gmail 계정의 비밀번호를 입력
    }
  });

  let mailOptions = {
    from: 'LostAndFind',    //발송 메일 주소 (위에서 작성한 gmail 계정 아이디)
    to: userEmail,                      // 수신 메일 주소
    subject: 'Sending Email using Node.js',   // 제목
    //text: 'That was easy',           // 내용
    html: '<!DOCTYPE html>' + 
    '<html>' +
      '<head>' + 
      '</head>' + 
      '<body>' + 
        '<h1>LostAndFind에서 인증번호를 보내드립니다.~~~~</h1>'+
        '<h2>' + pw + '</h2>' +
        '<h2> 인증번호 : ' + randNumber+ '</h2>' +  
      '</body>'+ 
    '</html>'
  };

  transporter.sendMail(mailOptions, function(error, info){
    if(error){
      console.log(error);
      return false;
    }
    else{
      console.log("random : ");
      console.log('Email sent: ' + info.response);
      console.log("인증번호가 발송되었습니다.");
      return true;
    }
  });
  res.json(randNumber);
});



module.exports = router;
