package com.alibaba.cola.statemachine;

/**
 * StateContext
 *  这个类的作用是啥？ 没有具体的实现
 * @author Frank Zhang
 * @date 2020-02-07 2:49 PM
 */
public interface StateContext<S, E, C> {
    /**
     * Gets the transition.
     *
     * @return the transition
     */
    Transition<S, E, C> getTransition();

    /**
     * Gets the state machine.
     *
     * @return the state machine
     */
    StateMachine<S, E, C> getStateMachine();
}
