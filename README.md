# News bot
This application is a simple Telegram bot that collect IT news 
from different websites and send them to a Telegram channel.

## Actions
The bot can:
- Collect news from subscribed websites every 2 hours.
I need this because RSS can contain a limited last news and if I request them
only when I ask the bot to publish news I can miss some of them.
- Send all collected news for the last 24 hours and last 7 days.
- Manage subscriptions(add, remove, getAll).

## Module structure
The bot has 6 modules:
- `news-bot-webapp` is a main web module. 
It contains controllers and some web configuration.
- `news-bot-logic` contain business logic. It provides all possible actions
that can do the bot.
- `news-bot-date` is a simple module to connect to the database via Hibernetes.
It contains entity classes and repositories.
- `news-bot-collect` is an RSS client to collect data from websites.
- `news-bot-telegram` is a Telegram webhook implementation.
- `news-bot-api` contains web clients or other open api to access the bot. 
Now it's empty because I don't have any other application that needs access to the bot.

I prefer this structure for projects because
it gives flexibility and scalability. It's easy to do any changes in any module
and they don't affect other parts of the service. Also, it's easy to replace any module.
For instance, I always can replace news-bot-data by news-bot-data-mongodb or
news-bot-telegram by news-bot-new-amazing-chat-app and don't touch other parts of the service.

## Some known issues and limitations
- **Security.** I have not found any proper way to secure a telegram webhook 
by standard web secure mechanism. I can provide only one protection. 
It's checking a chat id and a user id. 
Now both parameters are set up in app configuration. 

- **Only one user and one chat.** This bot is created for private usage that's why
it's not a problem However, in the future, it's possible to add 
a user registration and provide different subscriptions for different users.

## How to use
Here are 2 tricky parts. It's a telegram bot configuration and local deploy using NGrok.

### Telegram bot configuration
You have to create an own telegram bot and set up it a bit.
1. Use [BotFather](https://telegram.me/BotFather) and create a bot.
2. Add commands
   1. `receive_24h_news` - Receive all subscribed news for the last 24 hours 
   2. `receive_7days_news` - Receive all subscribed news for the last 7 days 
   3. `add_subscription` - Add new subscription. Format 'add_subscription -nName -uUrl' 
   4. `delete_subscription` - Delete the subscription with the name. Format 'delete_subscription -nName' 
   5. `get_all_subscriptions` - Return all registered subscriptions
3. Prohibit adding the bot to groups
   1. Open a session with [BotFather](https://telegram.me/BotFather).
   2. Enter `/setjoingroups`.
   3. Enter the name of your bot. `@example_blog_bot`.
   4. Enter `Disable`
4. Enable the privacy feature.
‘Enable’ - your bot will only receive messages that either start 
with the ‘/’ > symbol or mention the bot by username. 
‘Disable’ - your bot will receive all messages that people send to groups.
   1. Open a session with [BotFather](https://telegram.me/BotFather).
   2. Enter `/setprivacy`.
   3. Enter the name of the bot`@example_blog_bot`
   4. Enter `Enable`

### Ngrok configuration
You can run the application locally but Telegram needs access to your application.
I use Ngrok for this. 
1. Create account in Ngrok and generate a unique url.
2. Add `NGROK_URL` and `NGROK_AUTHTOKEN` in your local variables.
3. Execute docker_compose
4. Set up a webhook
   1. Add a local variable
   `NEWS_TBOT_WEBHOOK=<ngrok_url>/news-bot/telegram-webhook`
   2. Register the webhook in your Telegram bot 
   To ensure your Telegram bot knows where to send the incoming messages, you need to update its configuration. Use your preferred method, such as cURL, to send a request to the Telegram Bot API and set the webhook URL. Here’s an example using cURL:

      ```bash
      curl -X POST "https://api.telegram.org/bot<bot_token>/setWebhook?url=<ngrok_url>/<web_hook_path>"
      ```
    Replace `<bot_token>` with your actual Telegram bot token 
    and `<ngrok_url>` with the Ngrok URL you obtained earlier.

5. Send any message in Telegram to your bot. This message must be handled 
by your application endpoint. You can even open a local Ngrok client by http://localhost:4040/inspect/http 