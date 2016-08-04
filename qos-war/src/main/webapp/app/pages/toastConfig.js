/**
 * Created by NamNH on 6/25/2016.
 * http://jsfiddle.net/76dLm8h7/3/
 * http://jsfiddle.net/fwxbfu1u/6/
 * http://jsfiddle.net/jNc8E/
 */
var toastConfig = {
  autoDismiss: false,
  positionClass: 'toast-bottom-center',
  type: 'info',
  timeOut: '2000',
  extendedTimeOut: '500',
  allowHtml: false,
  closeButton: false,
  tapToDismiss: true,
  progressBar: true,
  newestOnTop: true,
  maxOpened: 0,
  preventDuplicates: false,
  preventOpenDuplicates: false,
  escapeHtml : true,
  title: "Well done!",
  msg: "You just run speed test completed."
};

var toastQuotesSetting = [
 	 {
 	   title: 'Well done!',
 	   message: 'You successfully update setting.',
 	   options: {
 	     allowHtml: true
 	   }
 	 },
 	 {
 	   title: 'Error!',
 	   message: 'The update setting has failed. Please try again!.',
 	   options: {
 	     allowHtml: true
 	   }
 	 },
 ];
    
var toastQuotesHistory = [
	 {
	   title: 'Well done!',
	   message: 'You just delete history record successful.',
	   options: {
	     allowHtml: true
	   }
	 },
	 {
	   title: 'Error!',
	   message: 'The search has failed. Please try again!',
	   options: {
	     allowHtml: true
	   }
	 },
	 {
	   title: 'Error!',
	   message: 'The search has failed. Please try again!',
	   options: {
	     allowHtml: true
	   }
	 },
	 {
	   title: 'Warning!',
	   message: 'The history data not found!',
	   options: {
	     allowHtml: true
	   }
	 }
];

var toastQuotesApp = [
      {
          title: 'Well done!',
          message: 'You just run speed test completed.',
          options: {
            allowHtml: true
          }
        },
        {
          title: 'No <em>login</em> yet!',
          message: 'Please <strong><a href="#/login">login</a></strong> to show your history',
          options: {
            allowHtml: true
          }
        },
        {
	        title: 'Good job!',
	        message: 'A history record added into system successfull.',
	        options: {
	          allowHtml: true
	        }
	      },
	      {
	          title: 'Error!',
	          message: 'Ping server fail. Please try again later.',
	          options: {
	            allowHtml: true
	          }
	        }
      ];

