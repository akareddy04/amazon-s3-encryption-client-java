package software.amazon.encryption.s3.utils;

import org.reactivestreams.Subscriber;
import software.amazon.awssdk.core.async.AsyncRequestBody;

import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * AsyncRequestBody which wraps another AsyncRequestBody with a {@link TinyBufferSubscriber}.
 * This is useful for testing poor network conditions where buffers may not be larger than
 * the cipher's block size.
 * DO NOT USE THIS IN PRODUCTION. In addition to degraded performance,
 * it will cause IllegalStateExceptions in the base Subscriber as it does not comply
 * with the Reactive Streaming spec.
 */
public class TinyBufferAsyncRequestBody implements AsyncRequestBody {

    private final AsyncRequestBody wrappedAsyncRequestBody;

    public TinyBufferAsyncRequestBody(final AsyncRequestBody wrappedRequestBody) {
        wrappedAsyncRequestBody = wrappedRequestBody;
    }

    @Override
    public Optional<Long> contentLength() {
        return wrappedAsyncRequestBody.contentLength();
    }

    @Override
    public void subscribe(Subscriber<? super ByteBuffer> s) {
        wrappedAsyncRequestBody.subscribe(new TinyBufferSubscriber(s));
    }
}
