/**
 * 图片水印处理工具
 */

/**
 * 给图片添加文字水印
 * @param {Object} options 配置项
 * @param {String} options.imagePath 图片路径
 * @param {String} options.text 水印文字（支持换行符）
 * @param {String} options.position 水印位置 topLeft/topRight/bottomLeft/bottomRight/center
 * @param {String} options.color 文字颜色
 * @param {Number} options.fontSize 字体大小
 * @param {Number} options.opacity 透明度 0-1
 * @param {Number} options.padding 边距
 * @returns {Promise<String>} 处理后的图片路径
 */
export function addTextWatermark(options) {
	return new Promise((resolve, reject) => {
		const {
			imagePath,
			text = '',
			position = 'bottomRight',
			color = '#FFFFFF',
			fontSize = 14,
			opacity = 0.8,
			padding = 10
		} = options
		
		// #ifdef APP-PLUS
		// APP端使用原生Canvas处理
		plus.nativeUI.showWaiting('处理中...')
		
		// 获取图片信息
		plus.io.resolveLocalFileSystemURL(imagePath, (entry) => {
			entry.file((file) => {
				const reader = new plus.io.FileReader()
				reader.onloadend = (e) => {
					const img = new Image()
					img.onload = () => {
						const canvas = document.createElement('canvas')
						const ctx = canvas.getContext('2d')
						
						canvas.width = img.width
						canvas.height = img.height
						
						// 绘制原图
						ctx.drawImage(img, 0, 0)
						
						// 设置水印样式
						ctx.font = `${fontSize}px Arial`
						ctx.fillStyle = color
						ctx.globalAlpha = opacity
						
						// 处理多行文本
						const lines = text.split('\n')
						const lineHeight = fontSize * 1.5
						const textWidth = Math.max(...lines.map(line => ctx.measureText(line).width))
						const textHeight = lines.length * lineHeight
						
						// 计算水印位置
						let x, y
						switch (position) {
							case 'topLeft':
								x = padding
								y = padding + fontSize
								break
							case 'topRight':
								x = canvas.width - textWidth - padding
								y = padding + fontSize
								break
							case 'bottomLeft':
								x = padding
								y = canvas.height - textHeight - padding
								break
							case 'bottomRight':
								x = canvas.width - textWidth - padding
								y = canvas.height - textHeight - padding
								break
							case 'center':
								x = (canvas.width - textWidth) / 2
								y = (canvas.height - textHeight) / 2 + fontSize
								break
							default:
								x = canvas.width - textWidth - padding
								y = canvas.height - textHeight - padding
						}
						
						// 绘制背景（可选）
						ctx.globalAlpha = 0.5
						ctx.fillStyle = '#000000'
						ctx.fillRect(x - 5, y - fontSize - 5, textWidth + 10, textHeight + 10)
						
						// 绘制文字
						ctx.globalAlpha = opacity
						ctx.fillStyle = color
						lines.forEach((line, index) => {
							ctx.fillText(line, x, y + index * lineHeight)
						})
						
						// 导出图片
						canvas.toBlob((blob) => {
							const fileName = `watermark_${Date.now()}.jpg`
							const tempPath = `_doc/watermark/${fileName}`
							
							plus.io.requestFileSystem(plus.io.PRIVATE_DOC, (fs) => {
								fs.root.getDirectory('watermark', {create: true}, (dirEntry) => {
									dirEntry.getFile(fileName, {create: true}, (fileEntry) => {
										fileEntry.createWriter((writer) => {
											writer.onwrite = () => {
												plus.nativeUI.closeWaiting()
												resolve(tempPath)
											}
											writer.onerror = reject
											writer.write(blob)
										}, reject)
									}, reject)
								}, reject)
							}, reject)
						}, 'image/jpeg', 0.9)
					}
					img.src = e.target.result
				}
				reader.readAsDataURL(file)
			}, reject)
		}, reject)
		// #endif
		
		// #ifdef H5
		// H5端使用Canvas处理
		const img = new Image()
		img.onload = () => {
			const canvas = document.createElement('canvas')
			const ctx = canvas.getContext('2d')
			
			canvas.width = img.width
			canvas.height = img.height
			
			// 绘制原图
			ctx.drawImage(img, 0, 0)
			
			// 设置水印样式
			ctx.font = `${fontSize}px Arial`
			ctx.fillStyle = color
			ctx.globalAlpha = opacity
			
			// 处理多行文本
			const lines = text.split('\n')
			const lineHeight = fontSize * 1.5
			const textWidth = Math.max(...lines.map(line => ctx.measureText(line).width))
			const textHeight = lines.length * lineHeight
			
			// 计算水印位置
			let x, y
			switch (position) {
				case 'topLeft':
					x = padding
					y = padding + fontSize
					break
				case 'topRight':
					x = canvas.width - textWidth - padding
					y = padding + fontSize
					break
				case 'bottomLeft':
					x = padding
					y = canvas.height - textHeight - padding
					break
				case 'bottomRight':
					x = canvas.width - textWidth - padding
					y = canvas.height - textHeight - padding
					break
				case 'center':
					x = (canvas.width - textWidth) / 2
					y = (canvas.height - textHeight) / 2 + fontSize
					break
				default:
					x = canvas.width - textWidth - padding
					y = canvas.height - textHeight - padding
			}
			
			// 绘制背景
			ctx.globalAlpha = 0.5
			ctx.fillStyle = '#000000'
			ctx.fillRect(x - 5, y - fontSize - 5, textWidth + 10, textHeight + 10)
			
			// 绘制文字
			ctx.globalAlpha = opacity
			ctx.fillStyle = color
			lines.forEach((line, index) => {
				ctx.fillText(line, x, y + index * lineHeight)
			})
			
			// 导出为base64
			resolve(canvas.toDataURL('image/jpeg', 0.9))
		}
		img.onerror = reject
		img.src = imagePath
		// #endif
		
		// #ifdef MP-WEIXIN
		// 微信小程序使用Canvas 2D处理
		const query = uni.createSelectorQuery()
		query.select('#watermark-canvas')
			.fields({ node: true, size: true })
			.exec((res) => {
				if (!res[0]) {
					// 如果没有canvas元素，使用原图
					console.warn('未找到canvas元素，使用原图')
					resolve(imagePath)
					return
				}
				
				const canvas = res[0].node
				const ctx = canvas.getContext('2d')
				
				// 获取图片信息
				uni.getImageInfo({
					src: imagePath,
					success: (imgInfo) => {
						canvas.width = imgInfo.width
						canvas.height = imgInfo.height
						
						// 创建图片对象
						const img = canvas.createImage()
						img.onload = () => {
							// 绘制原图
							ctx.drawImage(img, 0, 0, imgInfo.width, imgInfo.height)
							
							// 设置水印样式
							ctx.font = `${fontSize}px sans-serif`
							ctx.fillStyle = color
							ctx.globalAlpha = opacity
							
							// 处理多行文本
							const lines = text.split('\n')
							const lineHeight = fontSize * 1.5
							
							// 计算文本宽度
							let maxWidth = 0
							lines.forEach(line => {
								const metrics = ctx.measureText(line)
								if (metrics.width > maxWidth) {
									maxWidth = metrics.width
								}
							})
							const textWidth = maxWidth
							const textHeight = lines.length * lineHeight
							
							// 计算水印位置
							let x, y
							switch (position) {
								case 'topLeft':
									x = padding
									y = padding + fontSize
									break
								case 'topRight':
									x = canvas.width - textWidth - padding
									y = padding + fontSize
									break
								case 'bottomLeft':
									x = padding
									y = canvas.height - textHeight - padding
									break
								case 'bottomRight':
									x = canvas.width - textWidth - padding
									y = canvas.height - textHeight - padding
									break
								case 'center':
									x = (canvas.width - textWidth) / 2
									y = (canvas.height - textHeight) / 2 + fontSize
									break
								default:
									x = canvas.width - textWidth - padding
									y = canvas.height - textHeight - padding
							}
							
							// 绘制半透明背景
							ctx.globalAlpha = 0.5
							ctx.fillStyle = '#000000'
							ctx.fillRect(x - 5, y - fontSize - 5, textWidth + 10, textHeight + 10)
							
							// 绘制文字
							ctx.globalAlpha = opacity
							ctx.fillStyle = color
							lines.forEach((line, index) => {
								ctx.fillText(line, x, y + index * lineHeight)
							})
							
							// 导出图片
							uni.canvasToTempFilePath({
								canvas: canvas,
								success: (res) => {
									resolve(res.tempFilePath)
								},
								fail: reject
							})
						}
						img.src = imagePath
					},
					fail: reject
				})
			})
		// #endif
	})
}

/**
 * 获取当前位置信息文本
 * @returns {Promise<String>} 位置信息文本
 */
export async function getLocationText() {
	return new Promise((resolve) => {
		uni.getLocation({
			type: 'gcj02',
			geocode: true,
			success: (res) => {
				if (res.address) {
					// 如果有地址信息
					const addr = res.address
					const location = `${addr.province || ''}${addr.city || ''}${addr.district || ''}${addr.street || ''}${addr.streetNum || ''}`
					resolve(location || '未知位置')
				} else {
					// 只有经纬度
					resolve(`经度:${res.longitude.toFixed(6)}, 纬度:${res.latitude.toFixed(6)}`)
				}
			},
			fail: () => {
				resolve('位置获取失败')
			}
		})
	})
}

/**
 * 生成巡检水印文本
 * @param {Object} options 配置项
 * @returns {String} 水印文本
 */
export function generateInspectionWatermark(options = {}) {
	const {
		pointName = '',
		userName = uni.getStorageSync('userName') || '未知',
		deptName = uni.getStorageSync('deptName') || '',
		customText = ''
	} = options
	
	const now = new Date()
	const dateStr = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}`
	const timeStr = `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}:${String(now.getSeconds()).padStart(2, '0')}`
	
	let watermark = `日期: ${dateStr} ${timeStr}`
	
	if (pointName) {
		watermark += `\n地点: ${pointName}`
	}
	
	if (deptName) {
		watermark += `\n部门: ${deptName}`
	}
	
	watermark += `\n巡检员: ${userName}`
	
	if (customText) {
		watermark += `\n${customText}`
	}
	
	return watermark
}

/**
 * 创建带水印的Canvas组件（用于小程序）
 * @returns {Object} Canvas组件配置
 */
export function createWatermarkCanvas() {
	return {
		id: 'watermark-canvas',
		type: '2d',
		style: 'position: fixed; left: -9999px; top: -9999px; width: 2000px; height: 2000px;'
	}
}