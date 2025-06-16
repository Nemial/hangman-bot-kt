package handlers

import context.BotContext

interface TextHandler {
    suspend fun handle(context: BotContext)
}