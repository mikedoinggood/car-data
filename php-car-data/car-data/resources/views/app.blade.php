<!DOCTYPE html>
<html class="h-100">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="csrf-token" content="{{ csrf_token() }}">
	<link rel="stylesheet" href="/css/app.css">
        <title>@yield('title')</title>
        @yield('head')
    </head>
    <body class="d-flex flex-column h-100">
        @include('header')
        <div class="maincontent mb-4">
            @yield('content')
	</div>
	@include('footer')
        <script type="text/javascript" src="/js/script.js"></script>
	<script type="text/javascript" src="/js/app.js"></script>
        @yield('script')
    </body>
</html>
