var express = require('express');
var router = express.Router();

/* GET home page. */
router.post('/data', function(req, res, next) {
  console.log('index.js -> router post /data')
  console.log("type_question : "+req.body.type_question);  
  console.log("large_category : "+req.body.large_category);
  console.log("middle_category : "+req.body.middle_category);
  console.log("small_category : "+req.body.small_category);
  console.log("color_category : "+req.body.color_category);
  console.log("brand_name : "+req.body.brand_name);
  console.log("location : "+req.body.location);
  console.log("storage_location : "+req.body.storage_location);
  console.log("content : "+req.body.content);

  res.render('index', { title: 'Express' });
});

router.get('/androidSendData', function(req, res, next){
  console.log('index.js -> router get /androidSendData');

  var result = {1:'1', 2:'2', 3:'3'};

  res.json(result);
})

module.exports = router;
