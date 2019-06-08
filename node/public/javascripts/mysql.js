var mysql = require('mysql');
var connection = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    port: '3306',
    password: '2016225012',
    database: 'laf'
});

connection.connect();

module.exports = {
    select: function(sql, callback){
        connection.query(sql, function(error, rows, fields){
            if(error){
                console.log(error);
            }else{
                callback(rows);
            }
        });
    }
}
