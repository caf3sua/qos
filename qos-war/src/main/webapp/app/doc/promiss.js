"use strict";

Object.defineProperty(exports, "__esModule", {
	value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var XhrPromise = function () {
	_createClass(XhrPromise, null, [{
		key: "create",
		value: function create(options) {
			return new XhrPromise(options);
		}
	}]);

	function XhrPromise(options, promise) {
		_classCallCheck(this, XhrPromise);

		this.options = options;
		this.promise = promise || new Promise(this.executor.bind(this));
	}

	_createClass(XhrPromise, [{
		key: "executor",
		value: function executor(resolve, reject) {
			var xhr = new XMLHttpRequest();
			this.xhr = xhr;
			xhr.open(this.options.method, this.options.url);
			for (var h in this.options.headers) {
				if (this.options.headers.hasOwnProperty(h)) {
					xhr.setRequestHeader(h, this.options.headers[h]);
				}
			}
			xhr.onload = function () {
				if (this.readyState != 4) {
					return;
				}
				if (this.status >= 200 && this.status < 300) {
					resolve(this.responseText);
				} else {
					reject({
						status: this.status,
						statusText: this.statusText
					});
				}
			};
			xhr.onreadystatechange = function () {
				if (this.status == 0) {
					reject(XhrPromise.REJECT_RESPONSE);
				};
			};
			xhr.onerror = function () {
				reject({
					status: this.status,
					statusText: this.statusText
				});
			};
			xhr.onabort = function () {
				reject(XhrPromise.REJECT_RESPONSE);
			};
			xhr.send();
		}
	}, {
		key: "cancel",
		value: function cancel() {
			this.abort();
		}
	}, {
		key: "abort",
		value: function abort() {
			this.xhr.abort();
		}
	}, {
		key: "then",
		value: function then(a, b) {
			var p = new XhrPromise(this.options, this.promise.then(a, b));
			p.xhr = this.xhr;
			return p;
		}
	}], [{
		key: "REJECT_RESPONSE",
		get: function get() {
			return {
				status: -1,
				statusText: "Canceled"
			};
		}
	}]);

	return XhrPromise;
}();

exports.default = XhrPromise;
module.exports = exports["default"];
