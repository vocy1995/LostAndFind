var express = require('express');
var router = express.Router();
var newDate = require('date-utils');
var multer, storage, path, crypto;
var mysql = require("../public/javascripts/mysql");
multer = require('multer')
path = require('path');
crypto = require('crypto');
var NodeGeocoder = require('node-geocoder');

var ip='http://192.168.0.23:3000/upload/';

var options={
  provider: 'google',
  httpAdapter: 'https',
  apiKey: 'AIzaSyDl51o1oNDyv5urTP7ItU41XDuM8pez3gU', // for Mapquest, OpenCage, Google Premier
  formatter: null
};
var geocoder = NodeGeocoder(options);


storage = multer.diskStorage({
  destination: './upload',
  filename: function(req, file, cb) {
    return crypto.pseudoRandomBytes(16, function(err, raw) {
      if (err) {
        return cb(err);
      }
      return cb(null, "" + (raw.toString('hex')) + (path.extname(file.originalname)));
    });
  }
});

router.get('/', function(req, res, next){
  res.render('index');
});

//게시글 업로드 게시글 삽입
router.post('/createBoardText', function(req, res, next) {
  console.log('index.js -> router post /data');

  var title = req.body.title;
  var typeQuestion = req.body.type_question;
  var location = req.body.location;
  var content = req.body.content;
  var hashTag = req.body.hashTag;
  var latitude = req.body.latitude;
  var longitude = req.body.longitude;
  var board_writer = req.body.board_writer;
  //현재 날짜
  var dt = new Date();
  var time = dt.toFormat('YYYY년 MM월 DD일 HH24시 MI분 SS초');
  var countQuery = "select count(*) as count from laf.bullet_board";  //현재 게시글 숫자 확인
  mysql.select(countQuery, function(rows){
    var no = rows[0].count + 1;
    var insertQuery = "INSERT INTO laf.bullet_board (no, time, title, type_question, latitude, longitude, content, hash_tag, location,board_writer)"
      +"VALUES ('"+no+"', '"+time+"', '"+title+"', '"+typeQuestion+"', '"+latitude+"','"+longitude+"', '"+content+"', '"+hashTag+"', '"+location+"', '"+board_writer+"');";  //게시글 삽입 Query
    console.log(insertQuery);
    mysql.select(insertQuery, function(rows){});
  });

  res.json({"result":"ok"});
});

//게시물 작성 이미지 업로드
router.post('/createBoardImageUpload', multer({storage: storage}).single('upload'), function(req, res, next){
  console.log('------------------------------------------------');
  console.log('jjung.js router.post(/createBoardImageUpload)');
  console.log('------------------------------------------------');

  var countQuery = "select count(*) as count from laf.bullet_board";
  mysql.select(countQuery, function(countRows){
    var no = countRows[0].count;
    var selectTimeQuery = "select time from laf.bullet_board where no = '"+no+"'";
    mysql.select(selectTimeQuery, function(timeRows){
      var time = timeRows[0].time;
      var image_url_total=ip+req.file.filename;
      var updateQuery = "UPDATE laf.bullet_board SET img_url = '"+image_url_total+"' WHERE (no = '"+no+"') and (time = '"+time+"');";
      console.log(updateQuery);
      mysql.select(updateQuery, function(rows){
      });
    })
  });

  res.json({"result":"ok"})
});

router.post('/selectBulletinNum', function(req, res, next){
  var city = req.body.city;


  var selectQuery = 'select count(*) as count from laf.bullet_board where location like \'%'+city+'%\';';
  console.log(selectQuery);
  mysql.select(selectQuery, function(rows){
    console.log(rows[0].count)
    var result = rows[0].count;
    res.send(result.toString());
  });
});

module.exports = router;
