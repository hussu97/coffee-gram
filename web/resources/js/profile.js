changePrivacy = (userid) => {
      data = {
        userid: userid
      }
      $.post('privacy', data, (res) => {
        console.log(res);
        if (res == 'true')
          $('.privacyBtn').text('Change to Public');
        else
          $('.privacyBtn').text('Change to Private');
      })
    }
    changeFollowing = (followingid,followerid) => {
      data = {
        followingid: followingid,
        followerid: followerid
      }
      var followBtn = $('.followBtn');
      console.log(followBtn.text());
      if (followBtn.text().trim() === 'Follow') {
        data.DBaction = 'insert';
      } else {
        data.DBaction = 'remove'
      }
      $.post('following', data, (res) => {
        location.reload();
      })
    }