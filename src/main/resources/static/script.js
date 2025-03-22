setInterval(() => {
    location.reload();
}, 60000); // Refresh every 1 minute

// $(document).on('click', '.test', function () {
//     var postId = $(this).data('id');
//     console.log(postId);
//     $.ajax({
//         url: '/tasks/editTask/' + postId,
//         type: 'GET',
//         success: function (data) {
//             console.log(data);
//             $('#title').val(data.title);
//             $('#description').val(data.description);
//             $('#priority').val(data.priority);
//             $('#dueDate').val(data.dueDate);
//         },
//         error: function () {
//             alert('Failed to fetch post data. Please try again.');
//         }
//     });
// });
