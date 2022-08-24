package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.Transition;
import com.alibaba.cola.statemachine.Visitor;

import java.util.Collection;
import java.util.List;

/**
 * StateImpl
 *   状态变化由事件触发；
 *   InternalTransition不会引起状态变化
 * @author Frank Zhang
 * @date 2020-02-07 11:19 PM
 */
public class StateImpl<S,E,C> implements State<S,E,C> {
    protected final S stateId;
    private EventTransitions eventTransitions = new EventTransitions();

    StateImpl(S stateId){
        this.stateId = stateId;
    }

    /**
     * 创建并添加Transition，跟event绑定；状态机中由event触发Transition
     * 将Transition加入到Transitions中，保证不重复
     * @param event the event of the Transition
     * @param target the target of the transition
     * @param transitionType
     * @return
     */
    @Override
    public Transition<S, E, C> addTransition(E event, State<S,E,C> target, TransitionType transitionType) {
        Transition<S, E, C> newTransition = new TransitionImpl<>();
        newTransition.setSource(this);
        newTransition.setTarget(target);
        newTransition.setEvent(event);
        newTransition.setType(transitionType);

        Debugger.debug("Begin to add new transition: "+ newTransition);

        eventTransitions.put(event, newTransition);
        return newTransition;
    }

    @Override
    public List<Transition<S, E, C>> getEventTransitions(E event) {
        return eventTransitions.get(event);
    }

    @Override
    public Collection<Transition<S, E, C>> getAllTransitions() {
        return eventTransitions.allTransitions();
    }

    @Override
    public S getId() {
        return stateId;
    }

    @Override
    public String accept(Visitor visitor) {
        String entry = visitor.visitOnEntry(this);
        String exit = visitor.visitOnExit(this);
        return entry + exit;
    }

    @Override
    public boolean equals(Object anObject){
        if(anObject instanceof State){
            State other = (State)anObject;
            if(this.stateId.equals(other.getId()))
                return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return stateId.toString();
    }
}
