package handlers

import context.BotContext

interface CommandHandler {
    suspend fun handle(context: BotContext)
}