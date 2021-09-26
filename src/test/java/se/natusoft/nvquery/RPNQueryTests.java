package se.natusoft.nvquery;

import org.junit.jupiter.api.Test;
import se.natusoft.nvquery.api.DataQuery;
import se.natusoft.nvquery.data.providers.MapQueryData;
import se.natusoft.nvquery.rpn.RPNQuery;

public class RPNQueryTests
{
    private final DataQuery query = new RPNQuery();

    private final MapQueryData mapQueryData = new MapQueryData()
            .add( "name", "test data" )
            .add( "version", "1.0" )
            .add( "int", "8" )
            .add( "boolt", "true" )
            .add( "boolf", "false" );

    //
    // Basic tests
    //

    @Test
    public void testEquals()
    {
        assert query.query( "name 'test_data' /=", mapQueryData );// Spaces must be '_'!
    }

    @Test
    public void testEqualsNumber()
    {
        assert query.query( "version 1.0 /=", mapQueryData );
    }

    @Test
    public void testNotEquals()
    {
        assert query.query( "int 7 /!=", mapQueryData );
        assert !query.query( "int 8 /!=", mapQueryData );
    }

    @Test
    public void testContains()
    {
        assert query.query( "name 'data' /()", mapQueryData );
    }

    @Test
    public void testNotContains()
    {
        assert query.query( "name 'Nisse' /!()", mapQueryData );
    }

    @Test
    public void testTrue()
    {
        assert query.query( "boolt /T", mapQueryData );
    }

    @Test
    public void testFalse()
    {
        assert query.query( "boolf /F", mapQueryData );
    }

    @Test
    public void testGreaterThan()
    {
        assert query.query( "int 5 />", mapQueryData );
    }

    @Test
    public void testLessThan()
    {
        assert query.query( "int 10 /<", mapQueryData );
    }

    @Test
    public void testGreaterThanEquals()
    {
        assert query.query( "int 8 />=", mapQueryData );
    }

    @Test
    public void testLessThanEquals()
    {
        assert query.query( "int 8 /<=", mapQueryData );
    }

    //
    // Complex tests
    //

    @Test
    public void testComplex()
    {
        MapQueryData data = new MapQueryData()
                .add( "name", "MyServiceId" )
                .add("type", "service")
                .add( "qwe", 92 )
                .add("rty", 236);

        assert query.query(
                ///---------1--------\ /---2----\ /3\/---4----\ /5\/------6-----\ /7\/8\/9\
                "name 'MyServiceId' /= qwe 100 /< /= rty 100 /> /= type 'service' /= /= /T",
                data
        );
        // 1. Value of name and 'MyServiceId' is equal. ==> true
        // 2. Value of qwe is less than 100. ==> true
        // 3. The last 2 operations have the same result (true in this case). Leaves one 'true' on the stack.
        // 4. Value of rty is greater than 100. ==> true
        // 5. The last 2 operations have same result (true).
        // 6. type contains 'service'.
        // 7. The last 2 operations have same result (true).
        // 8. The last 2 operations have same result (true). Shortening the stack.
        // 9. Checks that end result (last entry on stack) is true (should only be one true on the stack here).
    }
}
