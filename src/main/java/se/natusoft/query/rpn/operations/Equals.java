package se.natusoft.query.rpn.operations;

import se.natusoft.query.rpn.Operation;

public class Equals implements Operation {

    /**
     * Executes the operation on the 2 provided values.
     *
     * @param value1 First value.
     * @param value2 Second value.
     * @return true or false.
     */
    @Override
    public boolean execute( String value1, String value2 ) {
        return value1.equals( value2 );
    }
}
