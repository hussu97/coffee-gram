

    function addComment() {
      if ($('#add-comment-form').is(":visible")) {
        $('#add-comment-btn').text('Leave a Comment');
        $('#add-comment-form').hide();
      } else {
        $('#add-comment-btn').text('Cancel');
        $('#add-comment-form').show();
      }
    }

    function editComment(i) {
      var buttonElement = $('#comment-' + i + '-edit-button');
      var commentClass = 'comment-' + i + '-text';
      var commentElement = $('#comment-' + i + '-text');
      var encodedString = "&lt;input id='" + commentClass +
          "' type='text' class='form-control' placeholder='Add Text to Save' value='" + commentElement
          .text() + "'>";
        var decodedText = $("<p/>").html(encodedString).text(); 
      if (buttonElement.text().trim() === 'Edit') {
        buttonElement.text('Save');
        $(commentElement).replaceWith(decodedText);
      } else {
        var commentText = commentElement.val().trim();
        console.log(commentText);
        if (!(commentText === "")) {
          data = {
            commentid: i,
            text: commentText
          }
          $.post('comment', data, (res) => {
            buttonElement.text('Edit');
            var encodedString = '&lt;p id=' + commentClass + ">" + commentElement.val() +
              '&lt;/p>';
            var decodedText = $("<p/>").html(encodedString).text();
            console.log(decodedText)
            $('#comment-' + i + '-text').replaceWith(decodedText);
          })
        }
      }
    }
    function deleteComment(i) {
        if(confirm('Are you sure you want to do this?')){
            console.log(i);
        }
    }

