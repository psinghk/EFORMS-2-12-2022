/*=================================================
true = yes/enable
flase = no/disable
1000 = 1 seconds
=================================================*/

/*=================================================
preloader
=================================================*/
var __preloaderFadeOut = 1200; // fadeout
var __preloaderDelay = 800; // delay

/*=================================================
scroll to element
=================================================*/
var __scrollToSpeed = 1250; // scroll to element speed
var __scrollToEase = 'easeInOutCirc'; // easing type

/*=================================================
theme color nav
=================================================*/
var __navFadeInDelay = 500; // nav fade in delay
var __colorNav = true; // style nav background to theme color

/*=================================================
border
=================================================*/
var __border = true; // show border

/*=================================================
overlay
=================================================*/
var __overlayStyle = 2; // 1 = normal, 2 = gradient
var __overlayOpacity = 0.75; // overlay opacity, 0 to 1, 0 = disable overlay

/*=================================================
header style
=================================================*/
var __headerStyle = 1; // 1 = static image, 2 = slideshow, 3 = youtube video

/*=================================================
slideshow
=================================================*/
var __imageAmount = 3; // image amount
var __slideshowDuration = 5000; // duration
var __slideshowDelay = 800; // delay

/*=================================================
youtube video
=================================================*/
var __youtubeUrl = 'https://www.youtube.com/watch?v=GKc0TUElTZM'; // youtube video url
var __videoStart = 77; // start time (seconds)
var __videoEnd = 122; // end time (seconds)
var __videoLoop = true; // loop
var __videoMute = false; // mute on start

/*=================================================
header effect
=================================================*/
var __effect = 0; // 0 = disable, 1 = star, 2 = cloud

/*=================================================
audio
=================================================*/
var __audio = true; // audio toggle

/*=================================================
countdown
=================================================*/
var __countdown = true; // countdown toggle
var __countdownDate = "12/24/2015 23:59:59"; // 2015-12-24 23:59:59
var __countdownTimezone = "-8"; // timezone

/*=================================================
contact
=================================================*/
var __contactEmail = 'email@example.com'; // contact email address
var __contactSuccess = '<i class="icons fa fa-check valid"></i> message has been sent'; // success submit message
var __contactInputError = '<i class="icons fa fa-close error"></i> all fields are required'; // input error message
var __contactEmailError = '<i class="icons fa fa-close error"></i> email address is invalid'; // email error message

/*=================================================
subscribe
=================================================*/
var __subscribeEmail = 'email@example.com'; // subscribe email address
var __subscribeSuccess = '<i class="icons fa fa-check valid"></i> thank you for subscribing'; // subscribe success message
var __subscribeError = '<i class="icons fa fa-close error"></i> email address is invalid'; // subscribe error message

/* mailchimp */
var __mailChimp = false; // true = mailchimp form, false = php subscribe form
var __mailChimpUrl = 'MAILCHIMP_POST_URL_HERE'; // mailchimp post url

$.ajaxChimp.translations.eng = { // custom mailchimp message
  'submit': 'please wait',
  0: '<i class="icon fa fa-check"></i> we have sent you a confirmation email',
  1: '<i class="icon fa fa-close"></i> enter a valid e-mail address',
  2: '<i class="icon fa fa-close"></i> e-mail address is not valid',
  3: '<i class="icon fa fa-close"></i> e-mail address is not valid',
  4: '<i class="icon fa fa-close"></i> e-mail address is not valid',
  5: '<i class="icon fa fa-close"></i> e-mail address is not valid'
}

// dedault message for reference

// Submit Message
// 'submit': 'Submitting...'
// Mailchimp Responses
// 0: 'We have sent you a confirmation email'
// 1: 'Please enter a value'
// 2: 'An email address must contain a single @'
// 3: 'The domain portion of the email address is invalid (the portion after the @: )'
// 4: 'The username portion of the email address is invalid (the portion before the @: )'
// 5: 'This email address looks fake or invalid. Please enter a real email address'

/*=================================================
disable section, true = disable, false = enable
last value without comma
=================================================*/
var __disableSection = {
  service : false, // service
  about : false, // about
  philosophy : false, // philosophy
  subscribe : false, // subscribe
  contact1 : false, // contact form
  contact2 : false // contact info
};