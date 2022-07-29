const Game_Objects = [];

export class GameObject {
	constructor() {
		Game_Objects.push(this);

		this.timedelta = 0; //时间间隔

		this.has_called_start = false;
	}

	start() {
		// 只执行一次
	}
	update() {
		//除开始第一帧外,每帧执行一次
	}
	on_destroy() {
		//删除之前执行
	}
	destroy() {
		this.on_destroy();

		for (let i in Game_Objects) {
			//in  遍历下标
			const obj = Game_Objects[i];
			if (obj === this) {
				Game_Objects.splice(i);
				break;
			}
		}
	}
}

let last_timestamp; //上一执行时刻

const step = (timestamp) => {
	for (let obj of Game_Objects) {
		//of 遍历值
		if (!obj.has_called_start) {
			obj.has_called_start = true;
			obj.start();
		} else {
			obj.timedelta = timestamp - last_timestamp;
			obj.update();
		}
	}
	last_timestamp = timestamp;
	requestAnimationFrame(step);
};

requestAnimationFrame(step);
