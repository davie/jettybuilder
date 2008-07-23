Simple builder like wrapper around jetty.
I know, it's not hard to set up anyway, but it's not guessable, and i keep on forgetting.

Probably the most useful bit is SimpleServer, which lets you set up lots of interesting cases that you might want
to have an integration test interact with.  For example:
new SimpleServer(8080).sleeping().start();
start a new http server on port 8080, which will accept a connection,  but never return

