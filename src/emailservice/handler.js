const AWS = require("aws-sdk");

exports.handler = function(event, context) {
  AWS.config.update({ region: "us-east-1" });
  console.log('Handling confirmation email to', event);

  const guid = event.guid;
  const htmlBody = `
    <!DOCTYPE html>
    <html>
      <head>
      Reset Email
      </head>
      <body>
        <p>Reset Email</p>
      </body>
    </html>
  `;

  const textBody = `
    Here is your reset token ${guid}
  `;

  const params = {
    Destination: {
      ToAddresses: [event.email]
    },
    Message: {
      Body: {
        Html: {
          Charset: "UTF-8",
          Data: htmlBody
        },
        Text: {
          Charset: "UTF-8",
          Data: textBody
        }
      },
      Subject: {
        Charset: "UTF-8",
        Data: "Here is your reset pin!"
      }
    },
    Source: "Team 007 <christopher.paul.dryden@gmail.com>"
  };

  const sendPromise = new AWS.SES({ apiVersion: "2010-12-01" })
    .sendEmail(params)
    .promise();

  sendPromise
    .then(data => {
      console.log(data.MessageId);
      context.done(null, "Success");
    })
    .catch(err => {
      console.error(err, err.stack);
      context.done(null, "Failed");
    });
};
