@extends('app')
  
@section('title', 'Login')

@section('content')
    <div class="container">
        <div class="col-md-8 offset-md-2 col-lg-5 offset-lg-3">
            <form id="loginform" action="{{ route('login') }}" method="post">
                @csrf
                <h3>Login</h3>
                <p>&lpar;Login with Username &equals; &quot;user&quot;, Password &equals; &quot;password&quot;&rpar;</p>
                <br/>
                <div class="form-group">
                    <label for="name">Username:</label>
                    <input class="form-control" type="text" name="name" id="username" />
                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input class="form-control" type="password" name="password" id="password" />
                </div>
                @if ($errors->has('name'))
                    <div>
                        <p class="text-danger">Invalid username or password.</p>
                    </div>
                @endif
                <br/>
                <div class="form-group">
                    <input class="btn btn-block btn-lg btn-primary" type="submit" value="Sign In" />
                </div>
            </form>
        </div>
    </div>
    <script type="text/javascript">
        document.getElementById("username").focus();
    </script>
@endsection
