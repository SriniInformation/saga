package ir.saga.participant;

import ir.saga.command.LockTarget;
import ir.saga.command.common.paths.PathVariables;
import ir.saga.command.consumer.CommandHandlers;
import ir.saga.command.consumer.CommandMessage;
import ir.saga.message.Message;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class SagaCommandHandlerBuilder<C> implements AbstractSagaCommandHandlersBuilder {
    private final SagaCommandHandlersBuilder parent;
    private final SagaCommandHandler h;

    public SagaCommandHandlerBuilder(SagaCommandHandlersBuilder parent, SagaCommandHandler h) {
        super();
        this.parent = parent;
        this.h = h;
    }

    @Override
    public <C> SagaCommandHandlerBuilder<C> onMessageReturningMessages(Class<C> commandClass, Function<CommandMessage<C>, List<Message>> handler) {
        return parent.onMessageReturningMessages(commandClass, handler);
    }

    @Override
    public <C> SagaCommandHandlerBuilder<C> onMessageReturningOptionalMessage(Class<C> commandClass, Function<CommandMessage<C>, Optional<Message>> handler) {
        return parent.onMessageReturningOptionalMessage(commandClass, handler);
    }

    @Override
    public <C> SagaCommandHandlerBuilder<C> onMessage(Class<C> commandClass, Function<CommandMessage<C>, Message> handler) {
        return parent.onMessage(commandClass, handler);
    }

    @Override
    public <C> SagaCommandHandlerBuilder<C> onMessage(Class<C> commandClass, Consumer<CommandMessage<C>> handler) {
        return parent.onMessage(commandClass, handler);
    }

    public SagaCommandHandlerBuilder<C> withPreLock(BiFunction<CommandMessage<C>, PathVariables, LockTarget> preLock) {
        h.setPreLock((raw, pvs) -> preLock.apply(raw, pvs));
        return this;
    }

    public SagaCommandHandlerBuilder<C> withPostLock(PostLockFunction<C> postLock) {
        h.setPostLock((raw, pvs, m) -> postLock.apply(raw, pvs, m));
        return this;
    }

    public CommandHandlers build() {
        return parent.build();
    }
}
