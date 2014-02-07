var loggedIn = false;

$(function() {
  $("#container").hide();
  checkEngines();
  setEventHandlers();
});

function checkEngines() {
  $("#dlgEnginCheck").dialog({
    modal: true,
    closeOnEscape: false,
    open: function(event, ui) {
      $(".ui-dialog-titlebar-close").hide();
    }
  });

  $.ajax({
    url: "/database",
    type: "GET",
    success: function(data, textStatus, jqXHR) {
      if (data.success) {
        var engines = data.engines;
        if (data.successMessage) {
          showMessageOk("Welcome", data.successMessage, function() {
            showLogin(engines);
          });
        } else {
          showLogin(engines);
        }
      }
    }
  }).done(function() {
    $("#dlgEnginCheck").dialog('close');
  });
}

function showLogin(engines) {
  var select = $("form#frmLogin #engine");

  if (engines !== null && engines !== undefined) {
    for (var i = 0; i < engines.length; ++i) {
      var engine = engines[i];
      select.append($("<option/>", {value: engine.engineName}).append(engine.engineName));
    }
  }

  $("#dlgLogin").dialog({
    modal: true,
    width: "350px",
    closeOnEscape: false,
    open: function(event, ui) {
      $(".ui-dialog-titlebar-close").hide();
      setTimeout(function(){$("input#password").focus()}, 200);
      $("#frmLogin").submit(function() {
        try {
          sendLogin();
        } catch (e) {
          console.error(e.message);
        }
        return false;
      });
    }
  });
}

function sendLogin() {
  var engine = $("#engine").val();
  var password = $("#password").val();
  var reqData = {"engine": engine, "password": password};

  $.ajax({
    url: "/login",
    type: "POST",
    contentType: 'application/json',
    data: JSON.stringify(reqData),
    success: function(data, textStatus, jqXHR) {
      if (data.success) {
        if (data.loginSuccess) {
          loggedIn = true;
          $("#loggedInEngine").html("Database: " + reqData.engine);
          $("#loginBoard").html("").attr("class", "");          
          $("#dlgLogin").dialog('close');
          $("#container").show();
        } else {
          $("#loginBoard").html(data.loginMessage).attr("class", "warning");
        }
      } else {
        $("#loginBoard").html(data.errorMessage).attr("class", "error");        
      }
    }
  }).done(function() {
    
  });
}

function setEventHandlers() {
  // logout link
  $("span#logout a").click(function() {
    loggedIn = false;
    document.location.reload();
  });
  
  // new entry button
  $("span#btnNew").click(function() {
    showNewEntry();
  });
}

function showNewEntry() {
  var content = $("div#content");
  var dialog = $("div#dlgNewEntry");
  content.html(dialog.clone().attr("id", "dlgNewEntryInstance").show());
  $("#dlgNewEntryInstance form#frmNewEntry").submit(function(){
    sendNewEntry();
    return false;
  });
}

function sendNewEntry() {
  var title = $("#entTitle").val();
  var username = $("#entUsername").val();
  var password = $("#entPassword").val();
  var description = $("#entDescription").val();
  var url = $("#entURL").val();
  var ip = $("#entIP").val();
  var note = $("#entNote").val();
  var reqData = {
    "title": title, "username": username, "password": password, "description": description,
    "url": url, "ip": ip, "note": note
  };

  $.ajax({
    url: "/entry",
    type: "POST",
    contentType: 'application/json',
    data: JSON.stringify(reqData),
    success: function(data, textStatus, jqXHR) {
      if (data.success) {
        if (data.loginSuccess) {
          loggedIn = true;
          $("#loggedInEngine").html("Database: " + reqData.engine);
          $("#loginBoard").html("").attr("class", "");          
          $("#dlgLogin").dialog('close');
          $("#container").show();
        } else {
          $("#loginBoard").html(data.loginMessage).attr("class", "warning");
        }
      } else {
        $("#loginBoard").html(data.errorMessage).attr("class", "error");        
      }
    }
  }).done(function() {
    
  });
}