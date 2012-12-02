[![Build Status](http://craigcook.co.uk/build/job/Simple%20HTTP%20Proxy/badge/icon)](http://craigcook.co.uk/build/job/Simple%20HTTP%20Proxy/)

## DESCRIPTION

A simple HTTP proxy servlet written in Java, designed for use in integration tests e.g.

http://localhost:9001/proxy/search?btnG=1&pws=0&q=foo+bar
becomes
http://www.google.co.uk/search?btnG=1&pws=0&q=foo+bar


## Usage

    Proxy proxy = new Proxy("http://www.google.co.uk", request, response, allowedPaths);
