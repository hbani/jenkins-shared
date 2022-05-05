def call(s) {
  if (s.trim() == '') {
      return '';
  }

// Replace ' with '\'' (https://unix.stackexchange.com/a/187654/260156). Then enclose with '...'.
// 1) Why not replace \ with \\? Because '...' does not treat backslashes in a special way.
// 2) And why not use ANSI-C quoting? I.e. we could replace ' with \'
// and enclose using $'...' (https://stackoverflow.com/a/8254156/4839573).
// Because ANSI-C quoting is not yet supported by Dash (default shell in Ubuntu & Debian) (https://unix.stackexchange.com/a/371873).
'\'' + s.replace('\'', '\'\\\'\'') + '\''
}
