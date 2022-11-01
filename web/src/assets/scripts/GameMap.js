import { GameObject } from "./GameObject";
import { Wall } from "./Wall";

export class GameMap extends GameObject {
	constructor(ctx, parent, store) {
		super();

		this.ctx = ctx;
		this.parent = parent;
		this.L = 0;

		this.store = store;

		this.rows = 13;
		this.cols = 14;

		this.inner_walls_count = 25;

		this.walls = [];
	}

	create_walls() {
		const g = this.store.state.pk.gamemap;
		for (let r = 0; r < this.rows; r++) {
			for (let c = 0; c < this.cols; c++) {
				if (g[r][c]) {
					//画墙
					this.walls.push(new Wall(r, c, this));
				}
			}
		}
		return true;
	}

	add_listening_events() {
		if (this.store.state.record.is_record) {
			let k = 0;
			k;
			const interval_id = setInterval(() => {}, 300);
			clearInterval(interval_id);
		} else {
			this.ctx.canvas.focus();

			this.ctx.canvas.addEventListener("keydown", (e) => {
				let d = -1;
				if (e.key === "w") d = 0;
				else if (e.key === "d") d = 1;
				else if (e.key === "s") d = 2;
				else if (e.key === "a") d = 3;

				if (d >= 0) {
					this.store.state.pk.socket.send(
						JSON.stringify({
							event: "move",
							direction: d,
						})
					);
				}
			});
		}
	}

	start() {
		for (let i = 0; i < 1000; i++) if (this.create_walls()) break;
	}

	update_size() {
		this.L = parseInt(
			Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows)
		);
		this.ctx.canvas.width = this.L * this.cols;
		this.ctx.canvas.height = this.L * this.rows;
	}

	update() {
		this.update_size();
		this.render();
	}

	render() {
		const color_even = "#AAD751",
			color_odd = "#A2D149";
		for (let r = 0; r < this.rows; r++) {
			for (let c = 0; c < this.cols; c++) {
				if ((r + c) % 2 == 0) {
					this.ctx.fillStyle = color_even;
				} else {
					this.ctx.fillStyle = color_odd;
				}
				this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
			}
		}
	}
}
