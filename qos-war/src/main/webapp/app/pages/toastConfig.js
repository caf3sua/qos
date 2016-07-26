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
  timeOut: '3000',
  extendedTimeOut: '1000',
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
	   message: 'The history record cannot be deleted',
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

