package flow;

import java.util.concurrent.Flow.*;

public class Main2 {
    public static void main( String[] args ) {
        getCelsiusTemperatures( "New York" )
                .subscribe( new TempSubscriber() );
    }

    public static Publisher<TempInfo> getCelsiusTemperatures(String town) {
        return subscriber -> {
            TempProcessor processor = new TempProcessor();
            processor.subscribe( subscriber );
            processor.onSubscribe( new TempSubscription(processor, town) );
        };
    }
}
