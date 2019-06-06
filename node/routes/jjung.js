var express = require('express');
var router = express.Router();
var newDate = require('date-utils');
var multer, storage, path, crypto;
multer = require('multer')
path = require('path');
crypto = require('crypto');

var typeQuestion;

router.get('/', function(req, res, next){
  res.render('index');
});

//게시글 업로드 게시글 삽입
router.post('/createBoardText', upload.single('imagePath'),function(req, res, next) {
  console.log('index.js -> router post /data');

  var title = req.body.title;
  typeQuestion = req.body.type_question;
  var location = req.body.location;
  var content = req.body.content;
  var hashTag = req.body.hashTag;
  //현재 날짜
  var dt = new Date();
  var time = dt.toFormat('YYYY년 MM월 DD일 HH24시 MI분 SS초');

  console.log("title = " + title);
  console.log("typeQuestion = " + typeQuestion);
  console.log("location = " + location);
  console.log("content = " + content);
  console.log("hashTag = " + hashTag);
  console.log("imagePath = " + imagePath);
  console.log("time = " + time);

  res.render('index', { title: 'Express' });
});

//게시물 작성 이미지 업로드
router.post('/createBoardImageUpload', multer({storage: storage}).single('upload'), function(req, res, next){
  console.log('------------------------------------------------');
  console.log('jjung.js router.post(/createBoardImageUpload)');
  console.log('------------------------------------------------');

  var storageLocation;
  if(typeQuestion == "분실물"){
    storageLocation = './uploads/lostArticle'
  }else{
    storageLocation = './uploads/objectOfLearning'
  }
  storage = multer.diskStorage({
    destination: storageLocation,
    filename: function(req, file, cb) {
      return crypto.pseudoRandomBytes(16, function(err, raw) {
        if (err) {
          return cb(err);
        }
        return cb(null, "" + (raw.toString('hex')) + (path.extname(file.originalname)));
      });
    }
  });

  console.log(req.file.filename);

});
/*
app.post("/upload_image",
  multer({storage: storage}).single('upload'), function(req, res) {
    var board = 'select count(*) as count from bullet.board;;'
  mysql.count(user, function(res){

  console.log(res[0].count);

  });
    var board_no=parseInt(board_count);
    console.log(req.file);
    console.log(req.body);
    console.log("보드 넘버 : "+ board_no);
      res.redirect("/uploads/" + req.file.filename);
    multer_url = ip + req.file.filename;
    var image_update = 'UPDATE laf.bullet_board SET image_url = \'' + multer_url + '\' WHERE (\'no\' = \''+board_no+'\') and (\'time\' = \'분실물\')';
    ;

    mysql.insert_user(image_update,function(rows){

    });
    console.log("값 저장 : " + multer_url);
    return res.status(200).end();
  });
  */

module.exports = router;
