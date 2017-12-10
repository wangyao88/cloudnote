'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var initialise = function initialise() {
  var application = new Application();
  // application.scaleFactor = 2;
  application.clearOnRedraw = Application.FADE;
  application.fadeColour = 'rgba(0,0,0,.03)';
  application.fillColour = 'rgba(30,30,30,1)';
  application.onResize();
  var vfield = new VectorField();
  vfield.scale = 300;
  vfield.amplitude = 40;
  // vfield.debug = true;

  application.addActor(vfield);

  var maxNum = 1000;
  var num = 0;

  var addTracer = function addTracer(position, colour) {
    if (num > maxNum) return;

    num++;

    var tracer = new BranchTracer(position.x, position.y);
    tracer.field = vfield;
    var momentum = new Vector(Math.random(), Math.random());
    momentum.length = Math.random() * 2;
    tracer.momentum = momentum;
    tracer.friction = 0.97;

    if (colour) {
      tracer.colour = colour;
    } else {
      tracer.colour = 'RGBA(' + Math.round(Math.random() * 255) + ',' + 100 + Math.round(Math.random() * 155) + ',255,0.2)';
    }

    application.addActor(tracer);
    return tracer;
  };
  var seed = addTracer(new Vector(window.innerWidth / 2, window.innerHeight / 2), 'RGBA(255, 100, 100, 0.8)');
  seed.branchChance = 5;
  seed.friction = 0.985;
  seed.onBranch = addTracer;

  setInterval(function () {
    vfield.z = Math.random() * 10000;
  }, 10000);

  var stage = application.stage;
  document.body.appendChild(stage);
  application.onPointerMove({ clientX: window.innerWidth / 2, clientY: window.innerHeight / 2 });
  application.render();
  application.animating = true;

  // application.runFor(60 * 120);

  return;
};

var Application = function () {
  function Application() {
    _classCallCheck(this, Application);

    this.stage = document.createElement('canvas');

    this.animate = this.animate.bind(this);

    this.onResize = this.onResize.bind(this);
    this.onPointerDown = this.onPointerDown.bind(this);
    this.onPointerup = this.onPointerup.bind(this);
    this.onPointerMove = this.onPointerMove.bind(this);

    this.initialiseEvents();
  }

  Application.prototype.initialiseEvents = function initialiseEvents() {
    window.addEventListener('resize', this.onResize, false);
    document.addEventListener('pointerdown', this.onPointerDown, false);
    document.addEventListener('pointerup', this.onPointerup, false);
    document.addEventListener('pointermove', this.onPointerMove, false);
  };

  Application.prototype.deInitialiseEvents = function deInitialiseEvents() {
    window.removeEventListener('resize', this.onResize, false);
    document.removeEventListener('pointerdown', this.onPointerDown, false);
    document.removeEventListener('pointerup', this.onPointerup, false);
    document.removeEventListener('pointermove', this.onPointerMove, false);
  };

  Application.prototype.addActor = function addActor(actor) {
    if (actor instanceof Actor) {
      this.actors.push(actor);
    }
  };

  Application.prototype.runFor = function runFor(ticks) {
    var interval = 1 / 60;
    var i = 0;

    for (i; i < ticks; i++) {
      this.triggerEvent('application-animate', { now: this.now, then: this.then, interval: interval, application: this });

      this.render();
    }
  };

  Application.prototype.animate = function animate() {
    this.now = Date.now();
    var interval = this.now - this.then;

    this.triggerEvent('application-animate', { now: this.now, then: this.then, interval: interval, application: this });

    this.render();

    this.then = this.now;

    if (this.animating) {
      requestAnimationFrame(this.animate);
    }
  };

  Application.prototype.render = function render() {
    var _this = this;

    var dims = this.dimensions;

    if (this.clearOnRedraw == Application.CLEAR) {
      this.context.clearRect(0, 0, dims.width, dims.height);
    } else if (this.clearOnRedraw == Application.FADE) {
      this.context.fillStyle = this.fadeColour;
      this.context.fillRect(0, 0, dims.width, dims.height);
    }

    this.actors.forEach(function (actor) {
      actor.render(_this);
    });
  };

  Application.prototype.onResize = function onResize(e) {
    console.log('resize');
    this.dimensions = new Vector(window.innerWidth, window.innerHeight);
  };

  Application.prototype.onPointerDown = function onPointerDown(e) {};

  Application.prototype.onPointerup = function onPointerup(e) {};

  Application.prototype.onPointerMove = function onPointerMove(e) {
    var pointer = new Vector(e.clientX, e.clientY);
    this.triggerEvent('application-pointermove', { pointer: pointer });
  };

  Application.prototype.triggerEvent = function triggerEvent(event, data) {
    if (window.CustomEvent) {
      var event = new CustomEvent(event, { detail: data });
    } else {
      var event = document.createEvent('CustomEvent');
      event.initCustomEvent(event, true, true, data);
    }

    document.dispatchEvent(event);
  };

  _createClass(Application, [{
    key: 'actors',
    get: function get() {
      if (!this._actors) this._actors = [];

      return this._actors;
    }
  }, {
    key: 'scaleFactor',
    set: function set(value) {
      if (value >= 1) {
        this._scaleFactor = value;
        this.onResize();
      }
    },
    get: function get() {
      return this._scaleFactor || 1;
    }
  }, {
    key: 'dimensions',
    set: function set(value) {
      if (value instanceof Vector) {
        value.scale(this.scaleFactor);
        this.stage.width = value.width;
        this.stage.height = value.height;
        this.context.fillStyle = this.fillColour;
        this.context.fillRect(0, 0, this.dimensions.width, this.dimensions.height);
        this._dimensions = value;
      }
    },
    get: function get() {
      return this._dimensions || new Vector(0, 0);
    }
  }, {
    key: 'stage',
    set: function set(value) {
      if (value instanceof HTMLCanvasElement) {
        value.className = this.className;
        this._stage = value;
        this.context = this.stage.getContext('2d');
        this.context.fillStyle = this.fillColour;
        this.context.fillRect(0, 0, this.dimensions.width, this.dimensions.height);
        this.onResize();
      }
    },
    get: function get() {
      return this._stage || null;
    }
  }, {
    key: 'now',
    set: function set(value) {
      if (!isNaN(value)) this._now = value;
    },
    get: function get() {
      return this._now || 0;
    }
  }, {
    key: 'then',
    set: function set(value) {
      if (!isNaN(value)) this._then = value;
    },
    get: function get() {
      return this._then || 0;
    }
  }, {
    key: 'animating',
    set: function set(value) {
      if (value === true && this.animating !== true) {
        this._animating = true;

        this.now = Date.now();
        this.then = this.now;

        requestAnimationFrame(this.animate);
      }
    },
    get: function get() {
      return this._animating === true;
    }
  }, {
    key: 'fadeColour',
    set: function set(value) {
      this._fadeColour = value;
    },
    get: function get() {
      return this._fadeColour || 'rgba(255,255,255,.5)';
    }
  }, {
    key: 'fillColour',
    set: function set(value) {
      this._fillColour = value;
    },
    get: function get() {
      return this._fillColour || 'rgba(255,255,255,1)';
    }
  }, {
    key: 'clearOnRedraw',
    set: function set(value) {
      if ([Application.NOCLEAR, Application.CLEAR, Application.FADE].indexOf(value) > -1) {
        this._clearOnRedraw = value;
      }
    },
    get: function get() {
      return this._clearOnRedraw || Application.NOCLEAR;
    }
  }, {
    key: 'className',
    get: function get() {
      return 'drawer';
    }
  }]);

  return Application;
}();

Application.NOCLEAR = 0;
Application.CLEAR = 1;
Application.FADE = 2;

var Actor = function () {
  function Actor(x, y, w, h) {
    _classCallCheck(this, Actor);

    this.dimensions = new Vector(w, h);
    this.position = new Vector(x, y);
  }

  Actor.prototype.render = function render() {};

  _createClass(Actor, [{
    key: 'dimensions',
    set: function set(value) {
      if (value instanceof Vector) this._dimensions = value;
    },
    get: function get() {
      return this._dimensions || new Vector(0, 0);
    }
  }, {
    key: 'position',
    set: function set(value) {
      if (value instanceof Vector) this._position = value;
    },
    get: function get() {
      return this._position || new Vector(0, 0);
    }
  }]);

  return Actor;
}();

var VectorField = function (_Actor) {
  _inherits(VectorField, _Actor);

  function VectorField() {
    var x = arguments.length <= 0 || arguments[0] === undefined ? 0 : arguments[0];
    var y = arguments.length <= 1 || arguments[1] === undefined ? 0 : arguments[1];
    var w = arguments.length <= 2 || arguments[2] === undefined ? 0 : arguments[2];
    var h = arguments.length <= 3 || arguments[3] === undefined ? 0 : arguments[3];

    _classCallCheck(this, VectorField);

    var _this2 = _possibleConstructorReturn(this, _Actor.call(this, x, y, w, h));

    _this2.noise = new Noise();

    _this2.helpers = [];

    _this2.mousepos = new Vector(0, 0);

    _this2.onResize = _this2.onResize.bind(_this2);
    _this2.onPointerMove = _this2.onPointerMove.bind(_this2);

    // document.addEventListener('application-pointermove', this.onPointerMove, false);
    window.addEventListener('resize', _this2.onResize);
    _this2.onResize();
    return _this2;
  }

  VectorField.prototype.render = function render(application) {
    this.helpers.forEach(function (helper) {
      helper.render(application);
    });
  };

  VectorField.prototype.preDraw = function preDraw() {};

  VectorField.prototype.postDraw = function postDraw() {};

  VectorField.prototype.solveForPosition = function solveForPosition(v) {
    if (!v instanceof Vector) return;

    v = v.clone();
    v.x -= window.innerWidth / 2;
    v.y -= window.innerHeight / 2;

    var scale = this.scale;
    var amp = this.amplitude;

    //     let waveform = new Vector(Math.cos(v.x / scale) * amp, Math.sin(v.y / scale) * amp);
    //     return new Vector(waveform.y - waveform.x, -waveform.x - waveform.y);

    var envelope = this.amplitude;

    var noise = this.noise.noise(v.x / scale, v.y / scale, this.z) * scale;
    if (noise > envelope) noise = envelope;
    if (noise < -envelope) noise = -envelope;
    var noise1 = this.noise.noise(v.y / scale, v.x / scale, this.z / scale);
    var transV = new Vector(1, 0);
    transV.length = noise;
    transV.angle = noise1 * 10;
    return transV;

    var transv = v.subtractNew(this.mousepos);
    transv = new Vector(transv.y - transv.x, -transv.x - transv.y);

    transv.length *= 0.03;
    if (transv.length > 50) {
      transv.length = 50;
    }
    transv.length -= 50;
    transv.length *= -1;

    return transv;
  };

  VectorField.prototype.onPointerMove = function onPointerMove(e) {
    var _this3 = this;

    this.mousepos = e.detail.pointer;

    this.helpers.forEach(function (helper) {
      helper.vector = _this3.solveForPosition(helper.position);
    });
  };

  VectorField.prototype.onResize = function onResize(e) {
    if (!this.debug) return;

    this.helpers.forEach(function (helper) {
      helper.destroy();
    });
    this.helpers = [];

    var w = this.sampleWidth;
    var curpos = new Vector(0, 0);

    while (curpos.y < window.innerHeight + w) {
      curpos.x = 0;
      while (curpos.x < window.innerWidth + w) {
        this.helpers.push(new Arrow(curpos.x, curpos.y, 10, 10, this.solveForPosition(curpos)));
        curpos.x += w;
      }
      curpos.y += w;
    }
  };

  _createClass(VectorField, [{
    key: 'scale',
    set: function set(value) {
      if (value > 0) {
        this._scale = value;
      }
    },
    get: function get() {
      return this._scale || 500;
    }
  }, {
    key: 'amplitude',
    set: function set(value) {
      if (value > 0) {
        this._amplitude = value;
      }
    },
    get: function get() {
      return this._amplitude || 10;
    }
  }, {
    key: 'sampleWidth',
    set: function set(value) {
      if (value > 0) this._sampleWidth = value;
    },
    get: function get() {
      return this._sampleWidth || 30;
    }
  }, {
    key: 'z',
    set: function set(value) {
      if (value > 0) this._z = value;
    },
    get: function get() {
      return this._z || 30;
    }
  }, {
    key: 'mousepos',
    set: function set(value) {
      if (value instanceof Vector) this._mousepos = value;
    },
    get: function get() {
      return this._mousepos || new Vector(0, 0);
    }
  }, {
    key: 'debug',
    set: function set(value) {
      this._debug = value === true;
    },
    get: function get() {
      return this._debug === true;
    }
  }, {
    key: 'strokeStyle',
    get: function get() {
      return 'black';
    }
  }, {
    key: 'strokeWidth',
    get: function get() {
      return 0;
    }
  }]);

  return VectorField;
}(Actor);

var Tracer = function (_Actor2) {
  _inherits(Tracer, _Actor2);

  function Tracer() {
    var x = arguments.length <= 0 || arguments[0] === undefined ? 200 : arguments[0];
    var y = arguments.length <= 1 || arguments[1] === undefined ? 200 : arguments[1];
    var w = arguments.length <= 2 || arguments[2] === undefined ? 40 : arguments[2];
    var h = arguments.length <= 3 || arguments[3] === undefined ? 20 : arguments[3];

    _classCallCheck(this, Tracer);

    var _this4 = _possibleConstructorReturn(this, _Actor2.call(this, x, y, w, h));

    _this4.onAnimate = _this4.onAnimate.bind(_this4);

    document.addEventListener('application-animate', _this4.onAnimate, false);

    _this4.friction = 0.99;
    _this4.momentum = new Vector(1, 0);
    return _this4;
  }

  Tracer.prototype.onAnimate = function onAnimate(e) {
    var force = this.field.solveForPosition(this.position).multiplyScalar(0.01);
    var app = e.detail.application;
    var oldPosition = this.position.clone();
    var draw = true;

    this.momentum.add(force);
    this.momentum.multiplyScalar(this.friction);
    if (this.momentum.length < 1) this.momentum.length = 1;
    if (this.momentum.length > 20) this.momentum.length = 20;
    this.position.add(this.momentum);

    if (this.position.x < -this.dimensions.width) {
      this.position.x = app.dimensions.width + this.dimensions.width;
      draw = false;
    } else if (this.position.x > app.dimensions.width + this.dimensions.width) {
      this.position.x = -this.dimensions.width;
      draw = false;
    }
    if (this.position.y < -this.dimensions.height) {
      this.position.y = app.dimensions.height + this.dimensions.height;
      draw = false;
    } else if (this.position.y > app.dimensions.height + this.dimensions.height) {
      this.position.y = -this.dimensions.height;
      draw = false;
    }

    if (draw) {
      var context = app.context;
      var opacity = Math.abs((this.momentum.length - 10) / 20);
      // console.log(opacity, this.momentum.length);
      // console.log(oldPosition, this.position);

      context.beginPath();
      context.lineWidth = this.momentum.length / 2;
      context.strokeStyle = this.colour;
      context.moveTo(oldPosition.x, oldPosition.y);
      context.lineTo(this.position.x, this.position.y);
      context.stroke();
    }
  };

  _createClass(Tracer, [{
    key: 'colour',
    set: function set(value) {
      this._colour = value;
    },
    get: function get() {
      return this._colour || 'RGBA(255,255,255,0.1)';
    }
  }]);

  return Tracer;
}(Actor);

var BranchTracer = function (_Tracer) {
  _inherits(BranchTracer, _Tracer);

  function BranchTracer() {
    _classCallCheck(this, BranchTracer);

    return _possibleConstructorReturn(this, _Tracer.apply(this, arguments));
  }

  BranchTracer.prototype.onAnimate = function onAnimate(e) {
    _Tracer.prototype.onAnimate.call(this, e);

    if (Math.random() * 100 < this.branchChance) {
      this.onBranch(this.position);
    }
  };

  _createClass(BranchTracer, [{
    key: 'onBranch',
    set: function set(value) {
      if (typeof value == 'function') this._onBranch = value.bind(this);
    },
    get: function get() {
      return this._onBranch || function () {};
    }
  }, {
    key: 'branchChance',
    set: function set(value) {
      if (value > 0 && value <= 100) this._branchChance = value;
    },
    get: function get() {
      return this._branchChance || 0.2;
    }
  }]);

  return BranchTracer;
}(Tracer);

var Noise = function () {
  function Noise(r) {
    _classCallCheck(this, Noise);

    if (r == undefined) r = Math;
    this.grad3 = [[1, 1, 0], [-1, 1, 0], [1, -1, 0], [-1, -1, 0], [1, 0, 1], [-1, 0, 1], [1, 0, -1], [-1, 0, -1], [0, 1, 1], [0, -1, 1], [0, 1, -1], [0, -1, -1]];
    this.p = [];
    for (var i = 0; i < 256; i++) {
      this.p[i] = Math.floor(r.random() * 256);
    }
    // To remove the need for index wrapping, double the permutation table length
    this.perm = [];
    for (var i = 0; i < 512; i++) {
      this.perm[i] = this.p[i & 255];
    }
  }

  Noise.prototype.dot = function dot(g, x, y, z) {
    return g[0] * x + g[1] * y + g[2] * z;
  };

  Noise.prototype.mix = function mix(a, b, t) {
    return (1.0 - t) * a + t * b;
  };

  Noise.prototype.fade = function fade(t) {
    return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
  };

  Noise.prototype.noise = function noise(x, y, z) {
    // Find unit grid cell containing point
    var X = Math.floor(x);
    var Y = Math.floor(y);
    var Z = Math.floor(z);

    // Get relative xyz coordinates of point within that cell
    x = x - X;
    y = y - Y;
    z = z - Z;

    // Wrap the integer cells at 255 (smaller integer period can be introduced here)
    X = X & 255;
    Y = Y & 255;
    Z = Z & 255;

    // Calculate a set of eight hashed gradient indices
    var gi000 = this.perm[X + this.perm[Y + this.perm[Z]]] % 12;
    var gi001 = this.perm[X + this.perm[Y + this.perm[Z + 1]]] % 12;
    var gi010 = this.perm[X + this.perm[Y + 1 + this.perm[Z]]] % 12;
    var gi011 = this.perm[X + this.perm[Y + 1 + this.perm[Z + 1]]] % 12;
    var gi100 = this.perm[X + 1 + this.perm[Y + this.perm[Z]]] % 12;
    var gi101 = this.perm[X + 1 + this.perm[Y + this.perm[Z + 1]]] % 12;
    var gi110 = this.perm[X + 1 + this.perm[Y + 1 + this.perm[Z]]] % 12;
    var gi111 = this.perm[X + 1 + this.perm[Y + 1 + this.perm[Z + 1]]] % 12;

    // The gradients of each corner are now:
    // g000 = grad3[gi000];
    // g001 = grad3[gi001];
    // g010 = grad3[gi010];
    // g011 = grad3[gi011];
    // g100 = grad3[gi100];
    // g101 = grad3[gi101];
    // g110 = grad3[gi110];
    // g111 = grad3[gi111];
    // Calculate noise contributions from each of the eight corners
    var n000 = this.dot(this.grad3[gi000], x, y, z);
    var n100 = this.dot(this.grad3[gi100], x - 1, y, z);
    var n010 = this.dot(this.grad3[gi010], x, y - 1, z);
    var n110 = this.dot(this.grad3[gi110], x - 1, y - 1, z);
    var n001 = this.dot(this.grad3[gi001], x, y, z - 1);
    var n101 = this.dot(this.grad3[gi101], x - 1, y, z - 1);
    var n011 = this.dot(this.grad3[gi011], x, y - 1, z - 1);
    var n111 = this.dot(this.grad3[gi111], x - 1, y - 1, z - 1);
    // Compute the fade curve value for each of x, y, z
    var u = this.fade(x);
    var v = this.fade(y);
    var w = this.fade(z);
    // Interpolate along x the contributions from each of the corners
    var nx00 = this.mix(n000, n100, u);
    var nx01 = this.mix(n001, n101, u);
    var nx10 = this.mix(n010, n110, u);
    var nx11 = this.mix(n011, n111, u);
    // Interpolate the four results along y
    var nxy0 = this.mix(nx00, nx10, v);
    var nxy1 = this.mix(nx01, nx11, v);
    // Interpolate the two last results along z
    var nxyz = this.mix(nxy0, nxy1, w);

    return nxyz;
  };

  return Noise;
}();

var conversionFactor = 180 / Math.PI;

var radianToDegrees = function radianToDegrees(radian) {
  return radian * conversionFactor;
};
var degreesToRadian = function degreesToRadian(degrees) {
  return degrees / conversionFactor;
};

// Taken from https://github.com/wethegit/wtc-vector
/**
 * A basic 2D Vector class that provides simple algebraic functionality in the form
 * of 2D Vectors.
 *
 * We use Getters/setters for both principle properties (x & y) as well as virtual
 * properties (rotation, length etc.).
 *
 * @class Vector
 * @author Liam Egan <liam@wethecollective.com>
 * @version 0.1.1
 * @created Dec 19, 2016
 */

var Vector = function () {

  /**
   * The Vector Class constructor
   *
   * @constructor
   * @param {number} x 				The x coord
   * @param {number} y 				The y coord
   */

  function Vector(x, y) {
    _classCallCheck(this, Vector);

    this.x = x;
    this.y = y;
  }

  /**
   * Resets the vector coordinates
   *
   * @public
  * @param {number} x 				The x coord
  * @param {number} y 				The y coord
   */

  Vector.prototype.reset = function reset(x, y) {
    this.x = x;
    this.y = y;
  };

  /**
   * Clones the vector
   *
   * @public
   * @return {Vector}					The cloned vector
   */

  Vector.prototype.clone = function clone() {
    return new Vector(this.x, this.y);
  };

  /**
   * Adds one vector to another.
   *
   * @public
   * @chainable
   * @param  {Vector}  vector The vector to add to this one
   * @return {Vector}					Returns itself, modified
   */

  Vector.prototype.add = function add(vector) {
    this.x += vector.x;
    this.y += vector.y;
    return this;
  };
  /**
   * Clones the vector and adds the vector to it instead
   *
   * @public
   * @chainable
   * @param  {Vector}  vector The vector to add to this one
   * @return {Vector}					Returns the clone of itself, modified
   */

  Vector.prototype.addNew = function addNew(vector) {
    var v = this.clone();
    return v.add(vector);
  };

  /**
   * Adds a scalar to the vector, modifying both the x and y
   *
   * @public
   * @chainable
   * @param  {number}  scalar The scalar to add to the vector
   * @return {Vector}					Returns itself, modified
   */

  Vector.prototype.addScalar = function addScalar(scalar) {
    return this.add(new Vector(scalar, scalar));
  };
  /**
   * Clones the vector and adds the scalar to it instead
   *
   * @public
   * @chainable
   * @param  {number}  scalar The scalar to add to the vector
   * @return {Vector}					Returns the clone of itself, modified
   */

  Vector.prototype.addScalarNew = function addScalarNew(scalar) {
    var v = this.clone();
    return v.addScalar(scalar);
  };

  /**
   * Subtracts one vector from another.
   *
   * @public
   * @chainable
   * @param  {Vector}  vector The vector to subtract from this one
   * @return {Vector}					Returns itself, modified
   */

  Vector.prototype.subtract = function subtract(vector) {
    this.x -= vector.x;
    this.y -= vector.y;
    return this;
  };
  /**
   * Clones the vector and subtracts the vector from it instead
   *
   * @public
   * @chainable
   * @param  {Vector}  vector The vector to subtract from this one
   * @return {Vector}					Returns the clone of itself, modified
   */

  Vector.prototype.subtractNew = function subtractNew(vector) {
    var v = this.clone();
    return v.subtract(vector);
  };

  /**
   * Subtracts a scalar from the vector, modifying both the x and y
   *
   * @public
   * @chainable
   * @param  {number}  scalar The scalar to subtract from the vector
   * @return {Vector}					Returns itself, modified
   */

  Vector.prototype.subtractScalar = function subtractScalar(scalar) {
    return this.subtract(new Vector(scalar, scalar));
  };
  /**
   * Clones the vector and subtracts the scalar from it instead
   *
   * @public
   * @chainable
   * @param  {number}  scalar The scalar to add to the vector
   * @return {Vector}					Returns the clone of itself, modified
   */

  Vector.prototype.subtractScalarNew = function subtractScalarNew(scalar) {
    var v = this.clone();
    return v.subtractScalar(scalar);
  };

  /**
   * Divides one vector by another.
   *
   * @public
   * @chainable
   * @param  {Vector}  vector The vector to divide this by
   * @return {Vector}					Returns itself, modified
   */

  Vector.prototype.divide = function divide(vector) {
    if (vector.x !== 0) {
      this.x /= vector.x;
    } else {
      this.x = 0;
    }
    if (vector.y !== 0) {
      this.y /= vector.y;
    } else {
      this.y = 0;
    }
    return this;
  };
  /**
   * Clones the vector and divides it by the vector instead
   *
   * @public
   * @chainable
   * @param  {Vector}  vector The vector to divide the clone by
   * @return {Vector}					Returns the clone of itself, modified
   */

  Vector.prototype.divideNew = function divideNew(vector) {
    var v = this.clone();
    return v.divide(vector);
  };

  /**
   * Divides the vector by a scalar.
   *
   * @public
   * @chainable
   * @param  {number}  scalar The scalar to divide both x and y by
   * @return {Vector}					Returns itself, modified
   */

  Vector.prototype.divideScalar = function divideScalar(scalar) {
    var v = new Vector(scalar, scalar);
    return this.divide(v);
  };
  /**
   * Clones the vector and divides it by the provided scalar.
   *
   * @public
   * @chainable
   * @param  {number}  scalar The scalar to divide both x and y by
   * @return {Vector}					Returns the clone of itself, modified
   */

  Vector.prototype.divideScalarNew = function divideScalarNew(scalar) {
    var v = this.clone();
    return v.divideScalar(scalar);
  };

  /**
   * Multiplies one vector by another.
   *
   * @public
   * @chainable
   * @param  {Vector}  vector The vector to multiply this by
   * @return {Vector}					Returns itself, modified
   */

  Vector.prototype.multiply = function multiply(vector) {
    this.x *= vector.x;
    this.y *= vector.y;
    return this;
  };
  /**
   * Clones the vector and multiplies it by the vector instead
   *
   * @public
   * @chainable
   * @param  {Vector}  vector The vector to multiply the clone by
   * @return {Vector}					Returns the clone of itself, modified
   */

  Vector.prototype.multiplyNew = function multiplyNew(vector) {
    var v = this.clone();
    return v.multiply(vector);
  };

  /**
   * Multiplies the vector by a scalar.
   *
   * @public
   * @chainable
   * @param  {number}  scalar The scalar to multiply both x and y by
   * @return {Vector}					Returns itself, modified
   */

  Vector.prototype.multiplyScalar = function multiplyScalar(scalar) {
    var v = new Vector(scalar, scalar);
    return this.multiply(v);
  };
  /**
   * Clones the vector and multiplies it by the provided scalar.
   *
   * @public
   * @chainable
   * @param  {number}  scalar The scalar to multiply both x and y by
   * @return {Vector}					Returns the clone of itself, modified
   */

  Vector.prototype.multiplyScalarNew = function multiplyScalarNew(scalar) {
    var v = this.clone();
    return v.multiplyScalar(scalar);
  };

  /**
   * Alias of {@link Vector#multiplyScalar__anchor multiplyScalar}
   */

  Vector.prototype.scale = function scale(scalar) {
    return this.multiplyScalar(scalar);
  };
  /**
   * Alias of {@link Vector#multiplyScalarNew__anchor multiplyScalarNew}
   */

  Vector.prototype.scaleNew = function scaleNew(scalar) {
    return this.multiplyScalarNew(scalar);
  };

  /**
   * Rotates a vecor by a given amount, provided in radians.
   *
   * @public
   * @chainable
   * @param  {number}  radian The angle, in radians, to rotate the vector by
   * @return {Vector}					Returns itself, modified
   */

  Vector.prototype.rotate = function rotate(radian) {
    var x = this.x * Math.cos(radian) - this.y * Math.sin(radian);
    var y = this.x * Math.sin(radian) + this.y * Math.cos(radian);

    this.x = x;
    this.y = y;

    return this;
  };
  /**
   * Clones the vector and rotates it by the supplied radian value
   *
   * @public
   * @chainable
   * @param  {number}  radian The angle, in radians, to rotate the vector by
   * @return {Vector}					Returns the clone of itself, modified
   */

  Vector.prototype.rotateNew = function rotateNew(radian) {
    var v = this.clone();
    return v.rotate(radian);
  };

  /**
   * Rotates a vecor by a given amount, provided in degrees. Converts the degree
   * value to radians and runs the rotaet method.
   *
   * @public
   * @chainable
   * @param  {number}  degrees The angle, in degrees, to rotate the vector by
   * @return {Vector}						Returns itself, modified
   */

  Vector.prototype.rotateDeg = function rotateDeg(degrees) {
    return this.rotate(degreesToRadian(degrees));
  };
  /**
   * Clones the vector and rotates it by the supplied degree value
   *
   * @public
   * @chainable
  * @param  {number}  degrees The angle, in degrees, to rotate the vector by
   * @return {Vector}					 Returns the clone of itself, modified
   */

  Vector.prototype.rotateDegNew = function rotateDegNew(degrees) {
    return this.rotateNew(degreesToRadian(degrees));
  };

  /**
   * Alias of {@link Vector#rotate__anchor rotate}
   */

  Vector.prototype.rotateBy = function rotateBy(radian) {
    return this.rotate(radian);
  };
  /**
   * Alias of {@link Vector#rotateNew__anchor rotateNew}
   */

  Vector.prototype.rotateByNew = function rotateByNew(radian) {
    return this.rotateNew(radian);
  };

  /**
   * Alias of {@link Vector#rotateDeg__anchor rotateDeg}
   */

  Vector.prototype.rotateDegBy = function rotateDegBy(degrees) {
    return this.rotateDeg(degrees);
  };
  /**
   * Alias of {@link Vector#rotateDegNew__anchor rotateDegNew}
   */

  Vector.prototype.rotateDegByNew = function rotateDegByNew(radian) {
    return tjos.rotateDegNew(radian);
  };

  /**
   * Rotates a vector to a specific angle
   *
   * @public
   * @chainable
   * @param  {number}  radian The angle, in radians, to rotate the vector to
   * @return {Vector}					Returns itself, modified
   */

  Vector.prototype.rotateTo = function rotateTo(radian) {
    return this.rotate(radian - this.angle);
  };

  /**
   * Clones the vector and rotates it to the supplied radian value
   *
   * @public
   * @chainable
   * @param  {number}  radian The angle, in radians, to rotate the vector to
   * @return {Vector}					Returns the clone of itself, modified
   */

  Vector.prototype.rotateToNew = function rotateToNew(radian) {
    var v = this.clone();
    return v.rotateTo(radian);
  };

  /**
   * Rotates a vecor to a given amount, provided in degrees. Converts the degree
   * value to radians and runs the rotateTo method.
   *
   * @public
   * @chainable
   * @param  {number}  degrees The angle, in degrees, to rotate the vector to
   * @return {Vector}						Returns itself, modified
   */

  Vector.prototype.rotateToDeg = function rotateToDeg(degrees) {
    return this.rotateTo(degreesToRadian(degrees));
  };
  /**
   * Clones the vector and rotates it to the supplied degree value
   *
   * @public
   * @chainable
  * @param  {number}  degrees The angle, in degrees, to rotate the vector to
   * @return {Vector}					 Returns the clone of itself, modified
   */

  Vector.prototype.rotateToDegNew = function rotateToDegNew(degrees) {
    return this.rotateToNew(degreesToRadian(degrees));
  };

  /**
   * Normalises the vector down to a length of 1 unit
   *
   * @public
   * @chainable
   * @return {Vector}					Returns itself, modified
   */

  Vector.prototype.normalise = function normalise() {
    return this.divideScalar(this.length);
  };
  /**
   * Clones the vector and normalises it
   *
   * @public
   * @chainable
   * @return {Vector}					Returns a clone of itself, modified
   */

  Vector.prototype.normaliseNew = function normaliseNew() {
    return this.divideScalarNew(this.length);
  };

  /**
   * Calculates the distance between this and the supplied vector
   *
   * @param  {Vector} vector The vector to calculate the distance from
   * @return {number}        The distance between this and the supplied vector
   */

  Vector.prototype.distance = function distance(vector) {
    return this.subtractNew(vector).length;
  };

  /**
   * Calculates the distance on the X axis between this and the supplied vector
   *
   * @param  {Vector} vector The vector to calculate the distance from
   * @return {number}        The distance, along the x axis, between this and the supplied vector
   */

  Vector.prototype.distanceX = function distanceX(vector) {
    return this.x - vector.x;
  };

  /**
   * Calculated the distance on the Y axis between this and the supplied vector
   *
   * @param  {Vector} vector The vector to calculate the distance from
   * @return {number}        The distance, along the y axis, between this and the supplied vector
   */

  Vector.prototype.distanceY = function distanceY(vector) {
    return this.y - vector.y;
  };

  /**
   * Calculates the dot product between this and a supplied vector
   *
   * @example
   * // returns -14
   * new Vector(2, -3).dot(new Vector(-4, 2))
   * new Vector(-4, 2).dot(new Vector(2, -3))
   * new Vector(2, -4).dot(new Vector(-3, 2))
   *
   * @param  {Vector} vector The vector object against which to calculate the dot product
   * @return {number}        The dot product of the two vectors
   */

  Vector.prototype.dot = function dot(vector) {
    return this.x * vector.x + this.y * vector.y;
  };

  /**
   * Calculates the cross product between this and the supplied vector.
   *
   * @example
   * // returns -2
   * new Vector(2, -3).cross(new Vector(-4, 2))
   * new Vector(-4, 2).cross(new Vector(2, -3))
   * // returns 2
   * new Vector(2, -4).cross(new Vector(-3, 2))
   *
   * @param  {Vector} vector The vector object against which to calculate the cross product
   * @return {number}        The cross product of the two vectors
   */

  Vector.prototype.cross = function cross(vector) {
    return this.x * vector.x - this.y * vector.y;
  };

  /**
   * Getters and setters
   */

  /**
   * (getter/setter) The x value of the vector.
   *
   * @type {number}
   * @default 0
   */

  _createClass(Vector, [{
    key: 'x',
    set: function set(x) {
      if (typeof x == 'number') {
        this._x = x;
      } else {
        throw new TypeError('X should be a number');
      }
    },
    get: function get() {
      return this._x || 0;
    }

    /**
    * (getter/setter) The y value of the vector.
    *
    * @type {number}
    * @default 0
    */

  }, {
    key: 'y',
    set: function set(y) {
      if (typeof y == 'number') {
        this._y = y;
      } else {
        throw new TypeError('Y should be a number');
      }
    },
    get: function get() {
      return this._y || 0;
    }

    /**
    * (getter/setter) The length of the vector presented as a square. If you're using
    * length for comparison, this is quicker.
    *
    * @type {number}
    * @default 0
    */

  }, {
    key: 'lengthSquared',
    set: function set(length) {
      var factor;
      if (typeof length == 'number') {
        factor = length / this.lengthSquared;
        this.multiplyScalar(factor);
      } else {
        throw new TypeError('length should be a number');
      }
    },
    get: function get() {
      return this.x * this.x + this.y * this.y;
    }

    /**
    * (getter/setter) The length of the vector
    *
    * @type {number}
    * @default 0
    */

  }, {
    key: 'length',
    set: function set(length) {
      var factor;
      if (typeof length == 'number') {
        factor = length / this.length;
        this.multiplyScalar(factor);
      } else {
        throw new TypeError('length should be a number');
      }
    },
    get: function get() {
      return Math.sqrt(this.lengthSquared);
    }

    /**
    * (getter/setter) The angle of the vector, in radians
    *
    * @type {number}
    * @default 0
    */

  }, {
    key: 'angle',
    set: function set(radian) {
      if (typeof radian == 'number') {
        this.rotateTo(radian);
      } else {
        throw new TypeError('angle should be a number');
      }
    },
    get: function get() {
      return Math.atan2(this.y, this.x);
    }

    /**
    * (getter/setter) The angle of the vector, in radians
    *
    * @type {number}
    * @default 0
    */

  }, {
    key: 'angleInDegrees',
    set: function set(degrees) {
      if (typeof degrees == 'number') {
        this.rotateToDeg(degrees);
      } else {
        throw new TypeError('angle should be a number');
      }
    },
    get: function get() {
      return radianToDegrees(Math.atan2(this.y, this.x));
    }

    /**
     * (getter/setter) Vector width.
      * Alias of {@link Vector#x x}
     *
     * @type {number}
     */

  }, {
    key: 'width',
    set: function set(w) {
      this.x = w;
    },
    get: function get() {
      return this.x;
    }

    /**
     * (getter/setter) Vector height.
      * Alias of {@link Vector#x x}
     *
     * @type {number}
     */

  }, {
    key: 'height',
    set: function set(h) {
      this.y = h;
    },
    get: function get() {
      return this.y;
    }

    /**
     * (getter/setter) Vector area.
     * @readonly
     *
     * @type {number}
     */

  }, {
    key: 'area',
    get: function get() {
      return this.x * this.y;
    }
  }]);

  return Vector;
}();