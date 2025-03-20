setInterval(() => {
    location.reload();
}, 60000); // Refresh every 1 minute

 // Reinitialize TinyMCE inside the edit modal
 $(document).on('click', '.test', function () {
     var postId = $(this).data('id');

     $.ajax({
         url: '/api/editPost/' + postId,
         type: 'GET',
         success: function (data) {
             $('#title').val(data.title);
             $('#description').val(data.description);
             $('#priority').val(data.priority);
              $('#dueDate').val(data.dueDate);

         },
         error: function () {
             alert('Failed to fetch post data. Please try again.');
         }
     });
 });
