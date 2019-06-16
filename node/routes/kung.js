var express = require('express');
var router = express.Router();
var newDate = require('date-utils');

var mysql = require("../public/javascripts/mysql");

var ip='http://localhost:3000/uploads/';

router.use(express.static('uploads'));
router.use('/uploads', express.static('uploads'));

router.get('/', function(req, res, next){
    res.render('index');
  });


router.post('/timeline', function(req, res, next) {
    console.log('index.js -> router post /data');

    var SelectQuery = 'select * from laf.bullet_board';  //현재 회원 숫자 확인
    console.log(SelectQuery);
    mysql.select(SelectQuery, function(rows){
        res.send(rows);
        console.log("rows : "+rows);
    });
});

router.post('/type', function(req, res, next) {
  console.log('index.js -> router post /data');

  var type = req.body.type_question;
  var location = req.body.location;

  var SelectQuery = 'select * from laf.bullet_board where type_question = \''+type+'\' and location like \'%'+location+'%\'';  //현재 회원 숫자 확인
  console.log(SelectQuery);
  mysql.select(SelectQuery, function(rows){
      res.send(rows);
      console.log("rows : "+rows);
  });
});

router.post('/mywrite', function(req, res, next) {
  console.log('index.js -> router post /data');

  var name = req.body.name;
  console.log("name : "+name);
  var SelectQuery = 'select * from laf.bullet_board where board_writer = \''+name+'\' ';  //현재 회원 숫자 확인
  console.log(SelectQuery);
  mysql.select(SelectQuery, function(rows){
      res.send(rows);
      console.log("rows : "+rows);
  });
});

router.post('/reply', function(req, res, next) {
  console.log('index.js -> router post /data');

  var comment = req.body.comment;
  var email = req.body.email;

  var SelectQuery = 'select * from laf.reply';  //현재 회원 숫자 확인
  console.log(SelectQuery);
  mysql.select(SelectQuery, function(rows){
      res.send(rows);
      console.log("rows : "+rows);
  });
});

router.post('/hash_tag', function(req, res, next) {
  console.log('index.js -> router post /data');

  var hash_tag = req.body.hash_tag;
  console.log("hash_tag : "+hash_tag);

  var SelectQuery = 'select * from laf.bullet_board where hash_tag like \'%'+hash_tag+'%\'';  //현재 회원 숫자 확인
  console.log(SelectQuery);

  mysql.select(SelectQuery, function(rows){
      res.send(rows);
      console.log("rows : "+rows);
  });
});

router.post('/replyPost', function(req, res, next) {
  console.log('index.js -> router post /data');
  var title_no = req.body.title_no;
  var name = req.body.writer;
  var content = req.body.content;
  console.log("writer test : "+ name);

    var countQuery = "select count(*) as count from laf.reply";  //현재 회원 숫자 확인

    mysql.select(countQuery, function(rows){
      var dt = new Date();
      var time = dt.toFormat('YYYY년 MM월 DD일 HH24시 MI분 SS초');
      var no = rows[0].count + 1;

      var insertQuery = "INSERT INTO laf.reply (no, title_no, content, writer,time)"
      +"VALUES ('"+no+"', '"+title_no+"', '"+content+"', '"+name+"','"+time+"');";  //회원 삽입 Query

    console.log(insertQuery);
    mysql.select(insertQuery, function(rows){});
  });
});


router.post('/findid', function(req, res, next) {
    console.log('index.js -> router post /data');

    var email = req.body.email;

    var SelectQuery = 'select id from laf.user where email = \''+email+'\';';  //현재 회원 숫자 확인
    console.log(SelectQuery);
    mysql.select(SelectQuery, function(rows){
        res.send(rows);
        console.log("rows : "+rows);
    });

});

router.post('/login', function(req, res, next) {
  console.log('index.js -> router post /data');

  var id = req.body.id;
  var pw = req.body.pw;

  var SelectQuery = 'select count(*) as count from laf.user where id = \''+id+'\' and pw = \''+pw+'\';';  //현재 회원 숫자 확인
  console.log(SelectQuery);
  mysql.select(SelectQuery, function(rows){
    if(rows[0].count==1){
      console.log("succeess2");
      var SelectQueryName = 'select name from laf.user where id = \''+id+'\' and pw = \''+pw+'\';';
      console.log(SelectQueryName);
      mysql.select(SelectQueryName, function(name){
        var name_test = name[0].name;
          console.log("name :"+name_test);
          res.send(name_test);
      });
    }
  });
});

router.post('/sign', function(req, res, next) {
    console.log('index.js -> router post /data');

    var name = req.body.name;
    var email = req.body.email;
    var id = req.body.id;
    var pw = req.body.pw;

    var countQuery = "select count(*) as count from laf.user";  //현재 회원 숫자 확인
    mysql.select(countQuery, function(rows){
      var no = rows[0].count + 1;
      var insertQuery = "INSERT INTO laf.user (no, name, email, id, pw)"
        +"VALUES ('"+no+"', '"+name+"', '"+email+"', '"+id+"', '"+pw+"');";  //회원 삽입 Query
      console.log(insertQuery);
      mysql.select(insertQuery, function(rows){});
    });

    res.send("success");
    res.end();
});



router.post('/idcheck', function(req, res, next) {
  console.log('index.js -> router post /data');

  var id = req.body.id;

  var SelectQuery = 'select count(*) as count from laf.user where id = \''+id+'\';';  //현재 회원 숫자 확인
  console.log(SelectQuery);
  mysql.select(SelectQuery, function(rows){
    if(rows[0].count==1){
      console.log("성공");
    }
  });

});
router.post('/reset', function(req, res, next) {
  console.log('index.js -> router post /data');

  var id = req.body.id;
  var pw = req.body.pw;

  var SelectQuery = 'select no from laf.user where id = \''+id+'\' and pw = \''+pw+'\';';
  console.log(SelectQuery);
  mysql.select(SelectQuery, function(rows){
    var no = rows;
    console.log("순서번호 : " +no);
  var updateQuery = "UPDATE laf.user SET pw = '"+pw+"' WHERE (no = '"+no+"');";
      console.log(updateQuery);
      mysql.select(updateQuery, function(rows){
        console.log(rows);
        res.send("성공");
    });
  });
});

module.exports = router;
