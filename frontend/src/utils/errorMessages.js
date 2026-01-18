const authMessages = {
  EMAIL_ALREADY_USED: 'Email уже используется',
  INVALID_CREDENTIALS: 'Неверный логин или пароль',
  EMAIL_NOT_VERIFIED: 'Email не подтвержден',
  USER_BLOCKED: 'Пользователь заблокирован',
  INVALID_REFRESH_TOKEN: 'Сессия истекла, войдите снова',
  INVALID_ACCESS_TOKEN: 'Токен доступа недействителен',
  TOKEN_EXPIRED: 'Срок действия токена истек',
  USER_NOT_FOUND: 'Пользователь не найден',
  VERIFICATION_TOKEN_INVALID: 'Неверный токен подтверждения',
  PASSWORD_RESET_TOKEN_INVALID: 'Неверный или просроченный токен',
  VALIDATION_ERROR: 'Некорректные данные',
}

export function resolveErrorMessage(error, fallback = 'Произошла ошибка') {
  const code = error?.response?.data?.error
  if (code && authMessages[code]) {
    return authMessages[code]
  }
  return error?.response?.data?.message || fallback
}
