package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.Transition;

/**
 * TransitionImpl。
 *  状态流转逻辑实现，是无状态的不可变对象，线程安全
 * This should be designed to be immutable, so that there is no thread-safe risk
 *
 * @author Frank Zhang
 * @date 2020-02-07 10:32 PM
 */
public class TransitionImpl<S,E,C> implements Transition<S,E,C> {

    private State<S, E, C> source;

    private State<S, E, C> target;

    private E event;

    private Condition<C> condition;

    private Action<S,E,C> action;

    private TransitionType type = TransitionType.EXTERNAL;

    @Override
    public State<S, E, C> getSource() {
        return source;
    }

    @Override
    public void setSource(State<S, E, C> state) {
        this.source = state;
    }

    @Override
    public E getEvent() {
        return this.event;
    }

    @Override
    public void setEvent(E event) {
        this.event = event;
    }

    @Override
    public void setType(TransitionType type) {
        this.type = type;
    }

    @Override
    public State<S, E, C> getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(State<S, E, C> target) {
        this.target = target;
    }

    @Override
    public Condition<C> getCondition() {
        return this.condition;
    }

    @Override
    public void setCondition(Condition<C> condition) {
        this.condition = condition;
    }

    @Override
    public Action<S, E, C> getAction() {
        return this.action;
    }

    @Override
    public void setAction(Action<S, E, C> action) {
        this.action = action;
    }

    /**
     *  1. 如果是InternalTransition，则状态不发生变化。
     *  2. 如果符合Condition，则执行后续Action
     *  3. 如果不符合Condition，则状态不变
     * @param ctx
     * @param checkCondition
     * @return
     */
    @Override
    public State<S, E, C> transit(C ctx, boolean checkCondition) {
        Debugger.debug("Do transition: "+this);
        this.verify();
        if (!checkCondition || condition == null || condition.isSatisfied(ctx)) {
            if(action != null){
                action.execute(source.getId(), target.getId(), event, ctx);
            }
            return target;
        }

        Debugger.debug("Condition is not satisfied, stay at the "+source+" state ");
        return source;
    }

    @Override
    public final String toString() {
        return source + "-[" + event.toString() +", "+type+"]->" + target;
    }

    @Override
    public boolean equals(Object anObject){
        if(anObject instanceof Transition){
            Transition other = (Transition)anObject;
            if(this.event.equals(other.getEvent())
                    && this.source.equals(other.getSource())
                    && this.target.equals(other.getTarget())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void verify() {
        if(type== TransitionType.INTERNAL && source != target) {
            throw new StateMachineException(String.format("Internal transition source state '%s' " +
                    "and target state '%s' must be same.", source, target));
        }
    }
}
