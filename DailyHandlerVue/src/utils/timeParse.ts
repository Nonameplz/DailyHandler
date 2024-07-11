export const timeParse = (time: string) => {
    const date = new Date(time)
    return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}/${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`
}

export const timeParseToday = (timeString: string) => {
    // 获取当前日期
    const currentDate = new Date()

    // 提取当前日期部分（不包括时间）
    const dateString = currentDate.toISOString().split('T')[0]

    // 合并日期和时间
    const dateTimeString = dateString + 'T' + timeString

    // 创建新的 Date 对象
    return new Date(dateTimeString)
}

export const dateToLocalTime = (dateString: Date) => {
    // 提取小时、分钟和秒
    const hours = dateString.getHours().toString().padStart(2, '0')
    const minutes = dateString.getMinutes().toString().padStart(2, '0')
    const seconds = dateString.getSeconds().toString().padStart(2, '0')

    // 生成时间字符串（HH:mm:ss格式）
    return `${hours}:${minutes}:${seconds}`
}
