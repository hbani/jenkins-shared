def call(s) {
  if (s.trim() == '') {
      return '';
  }
'\'' + s.replace('\'', '\'\\\'\'') + '\''
}
