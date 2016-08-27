/**
 * Created by NamNH on 4/6/2015.
 */
var SERVER_URL = "";

// Constant
var MAX_BANDWIDTH_MBPS = 100;
var BYTE_TO_MBPS = 1 / (1000 * 128);
var BYTE_TO_KBPS = 1 / 1000;
var MBPS_TO_KBPS = 128;

var DELTA_DIFFERENCE_MBPS = 5;
var DELTA_DIFFERENCE_KBPS = DELTA_DIFFERENCE_MBPS * 128;

var MAX_DURATION_TEST = 10 * 1000; // ms
var DURATION_UPLOAD = MAX_DURATION_TEST / 2;
var UPLOAD_TIME_SLEEP = 200;


// size sample
var UPLOAD_SIZE_SAMPLE = 1024 * 4;
var DOWNLOAD_SIZE_SAMPLE = 1024 * 10;

var BITRATE_TYPES = [
       {
     	id: 0,
         name: 'Kbps',
         href: '',
         enable: false,
         icon: 'socicon-stackoverflow',
         description: 'kilobits per second',
         note: 'n * 1024'
       },
       {
     	id: 1,
         name: 'Mbps',
         href: '',
         enable: false,
         icon: 'socicon-stackoverflow',
         description: 'megabits per second',
         note: 'n'
       }
];

// function common
 function calculateBandwidth(loadedSize, duration, unitType) {
	// Mbps
	if (unitType == undefined || unitType == null || unitType == 1) {
		return (loadedSize * BYTE_TO_MBPS)/ (duration / 1000);
	} else {
		return (loadedSize * BYTE_TO_KBPS)/ (duration / 1000);
	}
};

function randomString(len) {
    var charSet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    var randomString = '';
    for (var i = 0; i < len; i++) {
    	var randomPoz = Math.floor(Math.random() * charSet.length);
    	randomString += charSet.substring(randomPoz,randomPoz+1);
    }
    return randomString;
}

function normalizedDataPoint(latPoint, point, unitType) {
	// Check null
	if (latPoint == undefined || latPoint == null || point == null) {
		return;
	}
	
//	DELTA_DIFFERENCE_MBPS = 5;
//	var DELTA_DIFFERENCE_KBPS = DELTA_DIFFERENCE_MBPS * 128;
	var diff;
	// Mbps
	if (unitType == 1) {
		diff = DELTA_DIFFERENCE_MBPS;
	} else {
		diff = DELTA_DIFFERENCE_KBPS;
	}
	
	// Get latest point
	var delta = Math.abs(latPoint[1] - point[1]);
	
	if ((delta > diff) && (latPoint[1] > point[1])) {
		point[1] = latPoint[1] - diff;
	} else if ((delta > diff) && (latPoint[1] < point[1])) {
		point[1] = latPoint[1] + diff;
	}
	
	return point;
}