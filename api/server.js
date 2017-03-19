var express = require('express');
var fs = require("fs");
var request = require('request');
var cheerio = require('cheerio');
var app = express();

app.get('/off-peaky/:start/:end/:day', function (req, res) {
	
	var start = req.params.start;
	var end = req.params.end;
	var day = req.params.day;
	var url = `http://ojp.nationalrail.co.uk/service/timesandfares/${start}/${end}/${day}/0000/dep`;
	var data = new Array();
	request(url, function(error, response, html){
		if(!error){
			var $ = cheerio.load(html);			
			$('script[id^=jsonJourney]').filter(function(){
				var jsonString = $(this).text();
				var jsonObject = JSON.parse(jsonString);
				data.push(jsonObject.singleJsonFareBreakdowns);
			})
			res.send(data);
		}
    })
})

var server = app.listen(2403, function () {
  var port = server.address().port
  console.log("off-peaky listening on port %s", port)
})
