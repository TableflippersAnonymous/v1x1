#include "v1x1.h"
#include <stdio.h>
#include <stdarg.h>
#include <string.h>

struct v1x1_buffer v1x1_mkbuffer(int32_t length, char *data) {
  struct v1x1_buffer buffer = {
    length, data
  };
  return buffer;
}

struct v1x1_buffer v1x1_mkstrbuffer(char *str) {
  return v1x1_mkbuffer(strlen(str), str);
}

int32_t v1x1_send_string_message(struct v1x1_channel *channel, char *message) {
  struct v1x1_buffer buffer = v1x1_mkstrbuffer(message);
  return v1x1_send_message(channel, &buffer);
}

int32_t v1x1_printf_message(struct v1x1_channel *channel, char *format, ...) {
  char buf[4096];
  va_list args;
  va_start(args, format);
  vsnprintf(buf, 4096, format, args);
  int32_t ret = v1x1_send_string_message(channel, buf);
  va_end(args);
  return ret;
}