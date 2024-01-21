package buralek.newsbot.logic.command;

import buralek.newsbot.data.entity.Page;

import java.util.List;

public interface CommandHandler {
    List<Page> onReceive();
}
