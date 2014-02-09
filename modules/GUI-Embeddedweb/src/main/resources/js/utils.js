function showMessageOk(title, msg, okCallBack) {
  $("#dlgMessageOk").attr("title", title);
  $("#dlgMessageOk div#message").html(msg);
  $("#dlgMessageOk").dialog({
    modal: true,
    width: "350px",
    closeOnEscape: false,
    open: function(event, ui) {
      $(".ui-dialog-titlebar-close").hide();
    },
    buttons: {
      "OK": function() {
        $(this).dialog("close");
        okCallBack();
      }
    }
  });
}

function tagExists(tags, tag) {
  var result = false;
  
  if ($.isArray(tags)) {
    var len = tags.length;
    for (var i = 0; i < len; ++i) {
      var cTag = tags[i];
      
      if (cTag.title == tag.title) {
        result = true;
        break;
      }
    }
  }
  
  return result;
}

function checkPassword(input, passwordInputId) {  
    if (input.value != $("#" + passwordInputId).val()) {
        input.setCustomValidity('The two passwords must match.');
    } else {        
        input.setCustomValidity('');
   }
}