/*
  Author: bonefishcode
  Template: Bone-S
  Version: 1.0.1
  URL: http://themeforest.net/user/bonefishcode
*/

/*
do not edit this file
place custom function at assets/js/custom.js
*/

(function($) {
  'use strict';

/*=================================================
variable
=================================================*/
var $html = $('html');
var $body = $('body');

/*=================================================
IE10 viewport fix
=================================================*/
  (function() {
    'use strict';
    if (navigator.userAgent.match(/IEMobile\/10\.0/)) {
      var msViewportStyle = document.createElement('style')
      msViewportStyle.appendChild(
        document.createTextNode(
          '@-ms-viewport{width:auto!important}'
        )
      )
      document.querySelector('head').appendChild(msViewportStyle)
    }
  })();

/*=================================================
platform detect
=================================================*/
  var isMobile = /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
  var isIE8;
  var isIE9;
  (isMobile) ? $body.addClass('is-mobile') : $body.addClass('non-mobile');
  if ($html.hasClass('ie8')) {
    var isIE8 = true;
  }
  if ($html.hasClass('ie9')) {
    var isIE9 = true;
  }

/*=================================================
preloader
=================================================*/
  function _preloader() {
    $('#preloader-img').fadeOut(__preloaderFadeOut);
    $('#preloader').delay(__preloaderDelay).fadeOut(__preloaderFadeOut, function() {
      _animation();
      _border();
    });
  }

/*=================================================
button size
=================================================*/
  function _btnSize() {
    $('.btn-50').wrapInner("<span></span>");
  }

/*=================================================
set height
=================================================*/
  function _setHeight() {
    var vH = $(window).height();
    var $intro = $('#intro');
    if (!isMobile) {
      $intro.height(vH); /* set #intro 100vH */
    } else if ($intro.outerHeight() > vH) {
      $body.addClass('min-height');
      $intro.css('min-height', vH + 'px');
    } else if ($intro.outerHeight() <= vH) {
      $intro.css('height', vH + 'px');
    }
  }

/*=================================================
scroll to element
=================================================*/
  function _scrollTo() {
    $('a[href^="#"]').on('click', function(e) {
      var target = $( $(this).attr('href') );
      e.preventDefault();
      if (target.length) {
        $html.add($body).animate({
          scrollTop: target.offset().top
        }, __scrollToSpeed, __scrollToEase);
      }
    });
    $(window).on('beforeunload', function() {
      $(window).scrollTop(0);    
    });
  }

/*=================================================
animation
=================================================*/
  function _animation() {
    if (!isIE8 && !isIE9) {
      var wow = new WOW(
        {
          offset: 0
        }
      );
      wow.init();
    }
    if (!isIE8) {
      $('.nav-menu').delay(__navFadeInDelay).fadeIn();
    }
  }

/*=================================================
border
=================================================*/
  function _border() {
    if(!__border) {
      $body.addClass('border-off');
    } else {
      $body.addClass('border-on');
    }
  }

/*=================================================
countdown
=================================================*/
  function _countdown() {
    if (__countdown) {
      var $countdownContainer = $('#countdown');
      $countdownContainer.downCount({
        date: __countdownDate,
        offset: __countdownTimezone
      });
      $body.addClass('countdown-on'); /* add class to show countdown */
    }

  }

/*=================================================
header style toggle
=================================================*/
  function _toggle() {

    if (__headerStyle === 1) {
      _header();
    }
    if (__headerStyle === 2){
      _slideshow();
    }
    else if (__headerStyle === 3) {
      _video();
    }

  }

/*=================================================
static image header
=================================================*/
  function _header() {
    if (!isMobile) {
      $.backstretch('assets/img/bg/bg2.jpg');
    } else {
      $('#intro').backstretch('assets/img/bg/bg2.jpg');
    }
  }

/*=================================================
slideshow header
=================================================*/
  function _slideshow() {

    var slideShowImageSet = [];
    for (var i = 1; i <= __imageAmount; i++) {
      slideShowImageSet.push('assets/img/bg/slideshow-' + (i < 10 ? '0' + i : i) + '.jpg');
    }

    if (!isMobile) {
      $.backstretch(slideShowImageSet, {
        duration: __slideshowDuration, fade: __slideshowDelay
      });
    } else {
      $('#intro').backstretch(slideShowImageSet, {
        duration: __slideshowDuration, fade: __slideshowDelay
      });
    }
  }

/*=================================================
youtube video header
=================================================*/
  function _video() {

    if (isMobile) {

      $body.addClass('youtube-video-mobile');
      $('#intro').backstretch('assets/img/bg/video-mobile.jpg'); /* place image header on mobile */

    } else if (isIE8) {

      $.backstretch('assets/img/bg/video-desktop.jpg'); /* place image header on ie8 */

    } else {

      $body.addClass('youtube-video-desktop');
      var $bgVideo = $('#bg-video');
      var $play = $('#play');
      var $playIcon = $play.find('.icons');
      var $volume = $('#volume');
      var $volumeIcon = $volume.find('.icons');

      $.backstretch('assets/img/bg/video-desktop.jpg'); /* place image header before video start */
      $playIcon.addClass('fa-pause');

      $bgVideo.attr('data-property', '{videoURL: __youtubeUrl, showControls: false, autoPlay: true, loop: __videoLoop, mute: __videoMute, startAt: __videoStart, stopAt: __videoEnd, quality: "hd720", containment: "body"}');
      $bgVideo.YTPlayer();
      
      (__videoMute) ? $volumeIcon.addClass('fa-volume-off') : $volumeIcon.addClass('fa-volume-up');

      $play.on('click', function() {
        $playIcon.toggleClass('fa-play fa-pause', function() {
          ($playIcon.hasClass('fa-pause')) ? $bgVideo.pauseYTP() : $bgVideo.playYTP();
        }());
      });
      
      $volume.on('click', function() {
        $volumeIcon.toggleClass('fa-volume-off fa-volume-up', function() {
          ($volumeIcon.hasClass('fa-volume-up')) ? $bgVideo.muteYTPVolume() : $bgVideo.unmuteYTPVolume();
        }());
      });
    }

  }

/*=================================================
overlay
=================================================*/

  function _overlay() {
    var $overlays = $('.overlays');
    $overlays.css('opacity', __overlayOpacity);
    if (__overlayStyle == 1) {
      $overlays.addClass('overlay');
    }
    if (__overlayStyle == 2) {
      $overlays.addClass('gradient');
    }
  }

/*=================================================
audio
=================================================*/
  function _audioPlayer() {

    if (__audio) {

      if (isMobile) {
        var $audioPlayer = document.getElementById('audio-player'); // js
        var $play = $('#play');
        var $playIcon = $('#play').find('.icons');

        $body.addClass('audio-on');
        $playIcon.addClass('fa-play');
        $audioPlayer.pause();
        $play.on('click', function(e) {
          e.preventDefault();
          $playIcon.toggleClass('fa-play fa-pause', function() {
            ($playIcon.hasClass('fa-play')) ? $audioPlayer.play() : $audioPlayer.pause();
          }());
        });
      }

      if (__headerStyle != 3 && !isMobile && !isIE8) {
        var $audioPlayer = document.getElementById('audio-player'); // js
        var $play = $('#play');
        var $playIcon = $('#play').find('.icons');

        $body.addClass('audio-on');
        $playIcon.addClass('fa-pause');
        $audioPlayer.play();
        $play.on('click', function(e) {
          e.preventDefault();
          $playIcon.toggleClass('fa-play fa-pause', function() {
            ($playIcon.hasClass('fa-play')) ? $audioPlayer.play() : $audioPlayer.pause();
          }());
        });
      }
    }

  }

/*=================================================
email validation
=================================================*/
  function _formValidation(email_address) {
    var pattern = new RegExp(/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i);
    return pattern.test(email_address);
  }

/*=================================================
subscribe form
=================================================*/
  function _subscribe() {
    if (__mailChimp) {
      _mailChimp();
    } else {
      _subscribeForm();
    }
  }

  /* mailchimp */
  function _mailchimp() {

    var $form = $('#subscribe-form');
    var $subscribeEmail = $('#subscribe-email');

    $form.ajaxChimp({
      callback: _mailChimpStatus,
      language: 'eng',
      type: 'POST',
      url: __mailchimpUrl
    });

    function _mailChimpStatus (resp) {

      if (resp.result === 'error') {
        $subscribeEmail.focus();
        $('.subscribe-notice').addClass('visible');
      }
      else if (resp.result === 'success') {
        $form[0].reset();
        $subscribeEmail.blur();
        $('.subscribe-notice').addClass('visible');
      }

    }

  }

  /* php */
  function _subscribeForm() {

    var $form = $('#subscribe-form');
    var $subscribeEmail = $('#subscribe-email');

    $subscribeEmail.prop('type', 'text');

    $form.on('submit', function(e) {

      var subscribeEmailVal = $subscribeEmail.val();
      var $subscribeNotice = $('.subscribe-notice');
      var $submitButton = $form.find('button[type="submit"]');

      e.preventDefault();

      $submitButton.prop('disabled', true);

      if (!_formValidation(subscribeEmailVal)) {
        $subscribeNotice.stop(true).hide().addClass('visible').html(__subscribeError).fadeIn();
        $submitButton.prop('disabled', false);
        $('#subscribe-email').focus();
      }
      else {
        $.ajax({
          type: 'POST',
          url: 'assets/php/subscribe.php',
          data: {
            email: subscribeEmailVal,
            emailAddress: __subscribeEmail
          },
          success: function() {
            $subscribeNotice.stop(true).hide().addClass('visible').html(__subscribeSuccess).fadeIn();

            $submitButton.prop('disabled', false);
            $form[0].reset();
            $subscribeEmail.blur();

          }
        });
      }
      return false;

    });

  }

/*=================================================
contact form
=================================================*/
  function _contactForm() {

    var $form = $('#contact-form');

    $form.on('submit', function(e) {

      var $input = $form.find('input, textarea');
      var contactNameVal = $('#contact-name').val();
      var contactEmailVal = $('#contact-email').val();
      var contactMessageVal = $('#contact-message').val();
      var $contactNotice = $('.contact-notice');
      var $formNotice = $form.find('.form-notice');
      var $submitButton = $form.find('button[type="submit"]');

      e.preventDefault();

      if (contactNameVal == '' || contactEmailVal == '' || contactMessageVal == '') {

        $contactNotice.stop(true).hide().html(__contactInputError).fadeIn(500);

        $input.each(function() {
          if (this.value === '') {
            this.focus();
            return false;
          }
        });

      }

      else if (!_formValidation(contactEmailVal)) {

        $contactNotice.stop(true).hide().html(__contactEmailError).fadeIn(500);
        $('#contact-email').focus();

      }
      else {

        $.ajax({
          type: 'POST',
          url: 'assets/php/contact.php',
          data: {
            name: contactNameVal,
            email: contactEmailVal,
            message: contactMessageVal,
            emailAddress: __contactEmail
          },
          success: function() {

            $contactNotice.stop(true).hide().html(__contactSuccess).fadeIn(500);
            $form[0].reset();
            $input.blur();

          }
        });

      }
      return false;

    });
  }

/*=================================================
menu
=================================================*/
  function _menu() {
    var $navToggle = $('.nav-toggle');
    var $menuIcon = $('.menu-icon');
    var $menuIconA = $navToggle.add($menuIcon);
    $menuIconA.tooltip({placement: 'left'}); // tooltip
    $menuIconA.on('click', function(e) {
      e.preventDefault();
      $(this).blur(); // focus fadeout tooltip on click
      if ($navToggle.hasClass('nav-close')) {
        $navToggle.removeClass('nav-close').addClass('nav-open');
        $navToggle.find('i').removeClass('fa-navicon').addClass('fa-close');
        $menuIcon.each(function(i) {
          $(this).stop(true).delay((i++) * 50).fadeTo(300, 1);
        });
      } else if ($navToggle.hasClass('nav-open')) {
        $navToggle.removeClass('nav-open').addClass('nav-close');
        $($menuIcon.get().reverse()).each(function(i) {
          $(this).stop(true).delay((i++) * 50).fadeTo(300, 0, function() {
            $(this).hide();
          });
        });
      }
      return false;
    });
    if (__colorNav) {
      $body.addClass('color-nav');
    }
  }

/*=================================================
disable section
=================================================*/
  function _disableSection() {

    for (var a in __disableSection) {
      if (__disableSection[a]) {
        var id = '#' + a;
        $(id).remove();
        $('.menu-icon').find('a[href="'+id+'"]').parent().remove();
      }
    }

  }

/*=================================================
header effect
=================================================*/
 function _effect() {

    if (__effect==1 && !isMobile && !isIE8) {
      _star();
    }
    if (__effect==2 && !isMobile && !isIE8 && !isIE9) {
      _cloud();
    }

 }

/*=================================================
star
=================================================*/
  function _star() {

    /* requestAnimationFrame pollyfill */
    if (!window.requestAnimationFrame) {
      window.requestAnimationFrame = (window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.msRequestAnimationFrame || window.oRequestAnimationFrame || function (callback) {
        return window.setTimeout(callback, 1000 / 60);
      });
    }

    (function ($, window) {
    function Constellation (canvas, options) {
      var $canvas = $('#canvas'),
        context = canvas.getContext('2d'),
        defaults = {
          star: {
            color: 'rgba(255, 255, 255, 1)',
            width: 1
          },
          line: {
            color: 'rgba(255, 255, 255, 1)',
            width: 0.1
          },
          position: {
            x: 0, // This value will be overwritten at startup
            y: 0 // This value will be overwritten at startup
          },
          width: window.innerWidth,
          height: window.innerHeight,
          velocity: 0.1,
          length: 100,
          distance: 120,
          radius: 150,
          stars: []
        },
        config = $.extend(true, {}, defaults, options);

      function Star () {
        this.x = Math.random() * canvas.width;
        this.y = Math.random() * canvas.height;

        this.vx = (config.velocity - (Math.random() * 0.5));
        this.vy = (config.velocity - (Math.random() * 0.5));

        this.radius = Math.random() * config.star.width;
      }

      Star.prototype = {
        create: function(){
          context.beginPath();
          context.arc(this.x, this.y, this.radius, 0, Math.PI * 2, false);
          context.fill();
        },

        animate: function(){
          var i;
          for (i = 0; i < config.length; i++) {

            var star = config.stars[i];

            if (star.y < 0 || star.y > canvas.height) {
              star.vx = star.vx;
              star.vy = - star.vy;
            } else if (star.x < 0 || star.x > canvas.width) {
              star.vx = - star.vx;
              star.vy = star.vy;
            }

            star.x += star.vx;
            star.y += star.vy;
          }
        },

        line: function(){
          var length = config.length,
            iStar,
            jStar,
            i,
            j;

          for (i = 0; i < length; i++) {
            for (j = 0; j < length; j++) {
              iStar = config.stars[i];
              jStar = config.stars[j];

              if (
                (iStar.x - jStar.x) < config.distance &&
                (iStar.y - jStar.y) < config.distance &&
                (iStar.x - jStar.x) > - config.distance &&
                (iStar.y - jStar.y) > - config.distance
              ) {
                if (
                  (iStar.x - config.position.x) < config.radius &&
                  (iStar.y - config.position.y) < config.radius &&
                  (iStar.x - config.position.x) > - config.radius &&
                  (iStar.y - config.position.y) > - config.radius
                ) {
                  context.beginPath();
                  context.moveTo(iStar.x, iStar.y);
                  context.lineTo(jStar.x, jStar.y);
                  context.stroke();
                  context.closePath();
                }
              }
            }
          }
        }
      };

      this.createStars = function () {
        var length = config.length,
          star,
          i;

        context.clearRect(0, 0, canvas.width, canvas.height);

        for (i = 0; i < length; i++) {
          config.stars.push(new Star());
          star = config.stars[i];

          star.create();
        }

        star.line();
        star.animate();
      };

      this.setCanvas = function () {
        canvas.width = config.width;
        canvas.height = config.height;
      };

      this.setContext = function () {
        context.fillStyle = config.star.color;
        context.strokeStyle = config.line.color;
        context.lineWidth = config.line.width;
      };

      this.setInitialPosition = function () {
        if (!options || !options.hasOwnProperty('position')) {
          config.position = {
            x: canvas.width * 0.5,
            y: canvas.height * 0.5
          };
        }
      };

      this.loop = function (callback) {
        callback();

        window.requestAnimationFrame(function () {
          this.loop(callback);
        }.bind(this));
      };

      this.bind = function () {
        $canvas.on('mousemove', function(e){
          config.position.x = e.pageX - $canvas.offset().left;
          config.position.y = e.pageY - $canvas.offset().top;
        });
      };

      this.init = function () {
        this.setCanvas();
        this.setContext();
        this.setInitialPosition();
        this.loop(this.createStars);
        this.bind();
      };
    }

    $.fn.constellation = function (options) {
      return this.each(function () {
        var c = new Constellation(this, options);
        c.init();
      });
    };
    })($, window);

    $('canvas').constellation({
    line: {
      color: 'rgba(255, 255, 255, 1)'
    }
    });

  }

/*=================================================
window on load
=================================================*/
  $(window).on('load', function() {

    _preloader();
    _setHeight();
    _toggle();
    _overlay();
    _effect();

  });

/*=================================================
document on ready
=================================================*/
  $(document).on('ready', function() {

    _btnSize();
    _scrollTo();
    _countdown();
    _audioPlayer();
    _subscribe();
    _contactForm();
    _menu();
    _disableSection();

  });

/*=================================================
window on resize
=================================================*/
  $(window).on('resize', function() {
    if (!isMobile) {
      _setHeight();
    }
  }).trigger('resize');

})(jQuery);
