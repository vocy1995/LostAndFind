var express = require('express');
var router = express.Router();
var newDate = require('date-utils');

var mysql = require("../public/javascripts/mysql");


var Board;
var reply;
var reply_upload;
let user_count;
let board_count;
var inputData;
var multer_url;
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

router.post('/findid', function(req, res, next) {
    console.log('index.js -> router post /data');
    
    var name = req.body.name;
    var email = req.body.email;

    var SelectQuery = 'select id from laf.user where name = \''+name+'\' and email = \''+email+'\';';  //현재 회원 숫자 확인
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

  var SelectQuery = 'select count(*) as count from laf.user where name = \''+id+'\' and email = \''+pw+'\';';  //현재 회원 숫자 확인
  console.log(SelectQuery);    
  mysql.select(SelectQuery, function(rows){
    if(rows[0].count==1){
      console.log("succeess2");
      res.send("success");
      res.end();
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

module.exports = router;