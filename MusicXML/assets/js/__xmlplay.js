//~ Revision: 96, Copyright (C) 2016-2018: Willem Vree, contributions Stéphane David.
//~ This program is free software; you can redistribute it and/or modify it under the terms of the
//~ GNU General Public License as published by the Free Software Foundation; either version 2 of
//~ the License, or (at your option) any later version.
//~ This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
//~ without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//~ See the GNU General Public License for more details. <http://www.gnu.org/licenses/gpl.html>.
var $jscomp = $jscomp || {};
$jscomp.scope = {};
$jscomp.defineProperty = "function" == typeof Object.defineProperties ? Object.defineProperty : function (b, e, r) {
    if (r.get || r.set) throw new TypeError("ES3 does not support getters and setters.");
    b != Array.prototype && b != Object.prototype && (b[e] = r.value)
};
$jscomp.getGlobal = function (b) {
    return "undefined" != typeof window && window === b ? b : "undefined" != typeof global && null != global ? global : b
};
$jscomp.global = $jscomp.getGlobal(this);
$jscomp.SYMBOL_PREFIX = "jscomp_symbol_";
$jscomp.initSymbol = function () {
    $jscomp.initSymbol = function () {
    };
    $jscomp.global.Symbol || ($jscomp.global.Symbol = $jscomp.Symbol)
};
$jscomp.symbolCounter_ = 0;
$jscomp.Symbol = function (b) {
    return $jscomp.SYMBOL_PREFIX + (b || "") + $jscomp.symbolCounter_++
};
$jscomp.initSymbolIterator = function () {
    $jscomp.initSymbol();
    var b = $jscomp.global.Symbol.iterator;
    b || (b = $jscomp.global.Symbol.iterator = $jscomp.global.Symbol("iterator"));
    "function" != typeof Array.prototype[b] && $jscomp.defineProperty(Array.prototype, b, {
        configurable: !0,
        writable: !0,
        value: function () {
            return $jscomp.arrayIterator(this)
        }
    });
    $jscomp.initSymbolIterator = function () {
    }
};
$jscomp.arrayIterator = function (b) {
    var e = 0;
    return $jscomp.iteratorPrototype(function () {
        return e < b.length ? {done: !1, value: b[e++]} : {done: !0}
    })
};
$jscomp.iteratorPrototype = function (b) {
    $jscomp.initSymbolIterator();
    b = {next: b};
    b[$jscomp.global.Symbol.iterator] = function () {
        return this
    };
    return b
};
$jscomp.iteratorFromArray = function (b, e) {
    $jscomp.initSymbolIterator();
    b instanceof String && (b += "");
    var r = 0, c = {
        next: function () {
            if (r < b.length) {
                var z = r++;
                return {value: e(z, b[z]), done: !1}
            }
            c.next = function () {
                return {done: !0, value: void 0}
            };
            return c.next()
        }
    };
    c[Symbol.iterator] = function () {
        return c
    };
    return c
};
$jscomp.polyfill = function (b, e, r, c) {
    if (e) {
        r = $jscomp.global;
        b = b.split(".");
        for (c = 0; c < b.length - 1; c++) {
            var z = b[c];
            z in r || (r[z] = {});
            r = r[z]
        }
        b = b[b.length - 1];
        c = r[b];
        e = e(c);
        e != c && null != e && $jscomp.defineProperty(r, b, {configurable: !0, writable: !0, value: e})
    }
};
$jscomp.polyfill("Array.prototype.keys", function (b) {
    return b ? b : function () {
        return $jscomp.iteratorFromArray(this, function (b) {
            return b
        })
    }
}, "es6-impl", "es3");
$jscomp.polyfill("Math.log10", function (b) {
    return b ? b : function (b) {
        return Math.log(b) / Math.LN10
    }
}, "es6-impl", "es3");
$jscomp.makeIterator = function (b) {
    $jscomp.initSymbolIterator();
    var e = b[Symbol.iterator];
    return e ? e.call(b) : $jscomp.arrayIterator(b)
};
$jscomp.EXPOSE_ASYNC_EXECUTOR = !0;
$jscomp.FORCE_POLYFILL_PROMISE = !1;
$jscomp.polyfill("Promise", function (b) {
    function e() {
        this.batch_ = null
    }

    if (b && !$jscomp.FORCE_POLYFILL_PROMISE) return b;
    e.prototype.asyncExecute = function (b) {
        null == this.batch_ && (this.batch_ = [], this.asyncExecuteBatch_());
        this.batch_.push(b);
        return this
    };
    e.prototype.asyncExecuteBatch_ = function () {
        var b = this;
        this.asyncExecuteFunction(function () {
            b.executeBatch_()
        })
    };
    var r = $jscomp.global.setTimeout;
    e.prototype.asyncExecuteFunction = function (b) {
        r(b, 0)
    };
    e.prototype.executeBatch_ = function () {
        for (; this.batch_ && this.batch_.length;) {
            var b =
                this.batch_;
            this.batch_ = [];
            for (var F = 0; F < b.length; ++F) {
                var c = b[F];
                delete b[F];
                try {
                    c()
                } catch (E) {
                    this.asyncThrow_(E)
                }
            }
        }
        this.batch_ = null
    };
    e.prototype.asyncThrow_ = function (b) {
        this.asyncExecuteFunction(function () {
            throw b;
        })
    };
    var c = function (b) {
        this.state_ = 0;
        this.result_ = void 0;
        this.onSettledCallbacks_ = [];
        var n = this.createResolveAndReject_();
        try {
            b(n.resolve, n.reject)
        } catch (Q) {
            n.reject(Q)
        }
    };
    c.prototype.createResolveAndReject_ = function () {
        function b(b) {
            return function (n) {
                e || (e = !0, b.call(c, n))
            }
        }

        var c = this, e =
            !1;
        return {resolve: b(this.resolveTo_), reject: b(this.reject_)}
    };
    c.prototype.resolveTo_ = function (b) {
        if (b === this) this.reject_(new TypeError("A Promise cannot resolve to itself")); else if (b instanceof c) this.settleSameAsPromise_(b); else {
            var n;
            a:switch (typeof b) {
                case "object":
                    n = null != b;
                    break a;
                case "function":
                    n = !0;
                    break a;
                default:
                    n = !1
            }
            n ? this.resolveToNonPromiseObj_(b) : this.fulfill_(b)
        }
    };
    c.prototype.resolveToNonPromiseObj_ = function (b) {
        var c = void 0;
        try {
            c = b.then
        } catch (Q) {
            this.reject_(Q);
            return
        }
        "function" ==
        typeof c ? this.settleSameAsThenable_(c, b) : this.fulfill_(b)
    };
    c.prototype.reject_ = function (b) {
        this.settle_(2, b)
    };
    c.prototype.fulfill_ = function (b) {
        this.settle_(1, b)
    };
    c.prototype.settle_ = function (b, c) {
        if (0 != this.state_) throw Error("Cannot settle(" + b + ", " + c | "): Promise already settled in state" + this.state_);
        this.state_ = b;
        this.result_ = c;
        this.executeOnSettledCallbacks_()
    };
    c.prototype.executeOnSettledCallbacks_ = function () {
        if (null != this.onSettledCallbacks_) {
            for (var b = this.onSettledCallbacks_, c = 0; c < b.length; ++c) b[c].call(),
                b[c] = null;
            this.onSettledCallbacks_ = null
        }
    };
    var z = new e;
    c.prototype.settleSameAsPromise_ = function (b) {
        var c = this.createResolveAndReject_();
        b.callWhenSettled_(c.resolve, c.reject)
    };
    c.prototype.settleSameAsThenable_ = function (b, c) {
        var e = this.createResolveAndReject_();
        try {
            b.call(c, e.resolve, e.reject)
        } catch (E) {
            e.reject(E)
        }
    };
    c.prototype.then = function (b, e) {
        function n(b, c) {
            return "function" == typeof b ? function (c) {
                try {
                    E(b(c))
                } catch (ia) {
                    r(ia)
                }
            } : c
        }

        var E, r, F = new c(function (b, c) {
            E = b;
            r = c
        });
        this.callWhenSettled_(n(b,
            E), n(e, r));
        return F
    };
    c.prototype["catch"] = function (b) {
        return this.then(void 0, b)
    };
    c.prototype.callWhenSettled_ = function (b, c) {
        function e() {
            switch (n.state_) {
                case 1:
                    b(n.result_);
                    break;
                case 2:
                    c(n.result_);
                    break;
                default:
                    throw Error("Unexpected state: " + n.state_);
            }
        }

        var n = this;
        null == this.onSettledCallbacks_ ? z.asyncExecute(e) : this.onSettledCallbacks_.push(function () {
            z.asyncExecute(e)
        })
    };
    c.resolve = function (b) {
        return b instanceof c ? b : new c(function (c, e) {
            c(b)
        })
    };
    c.reject = function (b) {
        return new c(function (c,
                               e) {
            e(b)
        })
    };
    c.race = function (b) {
        return new c(function (e, r) {
            for (var n = $jscomp.makeIterator(b), z = n.next(); !z.done; z = n.next()) c.resolve(z.value).callWhenSettled_(e, r)
        })
    };
    c.all = function (b) {
        var e = $jscomp.makeIterator(b), r = e.next();
        return r.done ? c.resolve([]) : new c(function (b, n) {
            function z(c) {
                return function (e) {
                    E[c] = e;
                    F--;
                    0 == F && b(E)
                }
            }

            var E = [], F = 0;
            do E.push(void 0), F++, c.resolve(r.value).callWhenSettled_(z(E.length - 1), n), r = e.next(); while (!r.done)
        })
    };
    $jscomp.EXPOSE_ASYNC_EXECUTOR && (c.$jscomp$new$AsyncExecutor =
        function () {
            return new e
        });
    return c
}, "es6-impl", "es3");
var xmlplay_VERSION = 96, instUrl = "", instTab = {};
(function () {
    function b(a) {
        G.innerHTML += a + "\n"
    }

    function e(a) {
        var l = a.slice(0, 4E3);
        0 <= l.indexOf("X:") ? n(a) : -1 == l.indexOf("<?xml ") ? alert("not an xml file nor an abc file") : (a = (new window.DOMParser).parseFromString(a, "text/xml"), l = {
            p: "f",
            t: 1,
            u: 0,
            v: 3,
            m: 2,
            mnum: 0
        }, Ia && (l.p = ""), Ja && (l.x = 1), a = vertaal(a, l), a[1] && b(a[1]), n(a[0]))
    }

    function r() {
        var a, b = new FileReader;
        b.onload = function (a) {
            e(b.result)
        };
        if (a = ja ? ja[0] : W.files[0]) ka = a.name.split(".")[0], b.readAsText(a)
    }

    function c(a) {
        G.innerHTML = "";
        var l = a[0].link;
        ka = a[0].name.split(".")[0];
        u.style.display = "block";
        b("link: " + l + "<br>");
        var d = new XMLHttpRequest;
        d.open("GET", l, !0);
        d.onload = function () {
            b("XHR ok");
            u.style.display = "none";
            e(d.responseText)
        };
        d.onerror = function () {
            u.innerHTML += "XHR failed<br>"
        };
        d.send()
    }

    function z(a) {
        a.stopPropagation();
        a.preventDefault();
        ja = a.dataTransfer.files;
        this.classList.remove("indrag");
        r()
    }

    function n(a) {
        function b(a) {
            var b, d, l, f, h, k, p = {}, c, B = {}, e = [18, 20, 22, 24, 26, 28];
            a = a.split("\n");
            for (b = 0; b < a.length; ++b) if (d = a[b], 0 <= d.indexOf("strings") &&
            (l = d.match(/V:\s*(\S+).*strings\s*=\s*(\S+)/))) c = l[1], p[c] = {}, l[2].split(",").forEach(function (a, b) {
                f = a[0];
                h = 12 * parseInt(a[1]);
                k = h + [0, 2, 4, 5, 7, 9, 11]["CDEFGAB".indexOf(f)] + 12;
                p[c][e[b]] = k
            }), B[c] = 0 <= d.indexOf("diafret");
            return [p, B]
        }

        function d(a) {
            var b, d, l, f, h = {};
            a = a.split("\n");
            for (b = 0; b < a.length; ++b) if (d = a[b], 0 <= d.indexOf("%%map") && (l = d.match(/%%map *(\S+) *(\S+).*midi=(\d+)/))) d = l[1], f = l[2], l = l[3], h[d + f] = parseInt(l);
            return h
        }

        function c(a) {
            var b = {diamond: 1, triangle: 1, square: 1, normal: 1},
                d = ['%%beginsvg\n<defs>\n<text id="x" x="-3" y="0">&#xe263;</text>\n<text id="x-" x="-3" y="0">&#xe263;</text>\n<text id="x+" x="-3" y="0">&#xe263;</text>\n<text id="normal" x="-3.7" y="0">&#xe0a3;</text>\n<text id="normal-" x="-3.7" y="0">&#xe0a3;</text>\n<text id="normal+" x="-3.7" y="0">&#xe0a4;</text>\n<g id="circle-x"><text x="-3" y="0">&#xe263;</text><circle r="4" class="stroke"/></g>\n<g id="circle-x-"><text x="-3" y="0">&#xe263;</text><circle r="4" class="stroke"/></g>\n<path id="triangle" d="m-4 -3.2l4 6.4 4 -6.4z" class="stroke" style="stroke-width:1.4"/>\n<path id="triangle-" d="m-4 -3.2l4 6.4 4 -6.4z" class="stroke" style="stroke-width:1.4"/>\n<path id="triangle+" d="m-4 -3.2l4 6.4 4 -6.4z" class="stroke" style="fill:#000"/>\n<path id="square" d="m-3.5 3l0 -6.2 7.2 0 0 6.2z" class="stroke" style="stroke-width:1.4"/>\n<path id="square-" d="m-3.5 3l0 -6.2 7.2 0 0 6.2z" class="stroke" style="stroke-width:1.4"/>\n<path id="square+" d="m-3.5 3l0 -6.2 7.2 0 0 6.2z" class="stroke" style="fill:#000"/>\n<path id="diamond" d="m0 -3l4.2 3.2 -4.2 3.2 -4.2 -3.2z" class="stroke" style="stroke-width:1.4"/>\n<path id="diamond-" d="m0 -3l4.2 3.2 -4.2 3.2 -4.2 -3.2z" class="stroke" style="stroke-width:1.4"/>\n<path id="diamond+" d="m0 -3l4.2 3.2 -4.2 3.2 -4.2 -3.2z" class="stroke" style="fill:#000"/>\n</defs>\n%%endsvg'],
                l, f, h, c = "default", p = {"default": []};
            a = a.split("\n");
            for (l = 0; l < a.length; ++l) if (f = a[l], 0 <= f.indexOf("I:percmap") && (f = f.split(" "), h = f[4], h in b && (h = h + "+," + h), f = "%%map perc" + c + " " + f[1] + " print=" + f[2] + " midi=" + f[3] + " heads=" + h, p[c].push(f)), 0 <= f.indexOf("V:") && (h = f.match(/V:\s*(\S+)/))) c = h[1], c in p || (p[c] = []);
            for (c in p) d = d.concat(p[c]);
            for (l = 0; l < a.length; ++l) f = a[l], 0 <= f.indexOf("I:percmap") || (0 <= f.indexOf("V:") || 0 <= f.indexOf("K:") ? ((h = f.match(/V:\s*(\S+)/)) && (c = h[1]), 0 == p[c].length && (c = "default"), d.push(f),
            0 <= f.indexOf("perc") && -1 == f.indexOf("map=") && (f += " map=perc"), 0 <= f.indexOf("map=perc") && 0 < p[c].length && d.push("%%voicemap perc" + c), 0 <= f.indexOf("map=off") && d.push("%%voicemap")) : d.push(f));
            return d.join("\n")
        }

        0 <= a.indexOf("I:percmap") && (a = c(a));
        0 <= a.indexOf("%%map") && (Ka = d(a));
        0 <= a.indexOf(" strings") && b(a);
        la = a;
        F(a);
        Q(a)
    }

    function F(a) {
        function l(a) {
            var b = [];
            a.forEach(function (a, d) {
                b[a.st] ? b[a.st].push(d) : b[a.st] = [d];
                a.clef.clef_octave && (X[d] = a.clef.clef_octave);
                La[a.st] = 6 * (a.stafflines || "|||||").length *
                    (a.staffscale || 1);
                I[d] = a.midictl && a.midictl[7];
                void 0 == I[d] && (I[d] = 100);
                J[d] = a.midictl && a.midictl[10];
                void 0 == J[d] && (J[d] = 64);
                C[d] = a.instr ? a.instr : 0
            });
            return b
        }

        var d, c = "", k = [3, 0, 4, 1, 5, 2, 6], g = [0, 2, 4, 5, 7, 9, 11];
        M = [];
        X = [];
        H.value = N = O = 120;
        I = [];
        J = [];
        var C = [];
        d = new Abc({
            img_out: null, errmsg: function (a, b, d) {
                c += a + "\n"
            }, read_file: function (a) {
                return ""
            }, anno_start: null, get_abcmodel: function (b, d, c) {
                function f(a, b) {
                    p[a] = [0, 0, 0, 0, 0, 0, 0];
                    B[a] = {};
                    e[a] = b;
                    var d = 0 <= b;
                    (d ? k.slice(0, b) : k.slice(b)).forEach(function (b) {
                        p[a][b] +=
                            d ? 1 : -1
                    })
                }

                var p = {}, h = {"-2": -2, "-1": -1, 0: 0, 1: 1, 2: 2, 3: 0}, B = {}, e = {}, A = {},
                    r = {ppp: 30, pp: 45, p: 60, mp: 75, mf: 90, f: 105, ff: 120, fff: 127}, n = [], v;
                c = d[0].meter.a_meter;
                c.length && parseInt(c[0].top);
                for (w = 0; w < d.length; ++w) f(w, d[w].key.k_sf), A[w] = {};
                c = {};
                Ma = d.length;
                ma = l(d);
                for (var m = b; m; m = m.ts_next) {
                    var t, q, u, y = [], w;
                    switch (m.type) {
                        case 14:
                            b = m.tempo_notes.reduce(function (a, b) {
                                return a + b
                            });
                            O = m.tempo * b / 384;
                            0 == m.time && (H.value = N = O);
                            break;
                        case 10:
                            q = {t: m.time, mnum: -1, dur: m.dur};
                            y.push(q);
                            M.push({
                                t: m.time, ix: m.istart, v: m.v,
                                ns: y, inv: m.invis, tmp: O
                            });
                            break;
                        case 8:
                            var D = C[m.v];
                            "p" == m.p_v.clef.clef_type && (D += 128);
                            for (b = 0; b < m.notes.length; ++b) {
                                d = m.notes[b];
                                t = d.pit + 19;
                                w = m.v;
                                m.a_dd && m.a_dd.forEach(function (a) {
                                    (v = r[a.name]) && ma[m.st].forEach(function (a) {
                                        n[a] = v
                                    })
                                });
                                v = n[w] || 60;
                                X[w] && (t += X[w]);
                                q = Math.floor(t / 7);
                                u = t % 7;
                                void 0 != d.acc && (B[w][t] = h[d.acc]);
                                q = 12 * q + g[u] + (t in B[w] ? B[w][t] : p[w][u]);
                                u = m.p_v.map;
                                if (128 <= D && "MIDIdrum" != u) {
                                    var z = a.substring(m.istart, m.iend), z = z.match(/[=_^]*[A-Ga-g]/)[0];
                                    (u = Ka[u + z]) && (q = u)
                                }
                                q = 128 * D + q;
                                c[q] = 1;
                                q = {t: m.time, mnum: q, dur: m.dur, velo: v};
                                t in A[w] ? (A[w][t].dur += m.dur, 0 == d.ti1 && delete A[w][t]) : (0 != d.ti1 && (A[w][t] = q), y.push(q))
                            }
                            if (0 == y.length) break;
                            M.push({t: m.time, ix: m.istart, v: m.v, ns: y, stf: m.st, tmp: O});
                            break;
                        case 5:
                            f(m.v, m.k_sf);
                            break;
                        case 0:
                            f(m.v, e[m.v]);
                            M.push({t: m.time, ix: m.istart, v: m.v, bt: m.bar_type, tx: m.text});
                            break;
                        case 16:
                            m.instr && (C[m.v] = m.instr), 7 == m.ctrl && (I[m.v] = m.val), 10 == m.ctrl && (J[m.v] = m.val)
                    }
                }
                na.forEach(function (a) {
                    var b = a.parentNode;
                    b && b.removeChild(a)
                });
                Y = [];
                h = "#f9f #3cf #c99 #f66 #fc0 #cc0 #ccc".split(" ");
                for (b = 0; b < Ma; ++b) A = 1 << b & oa ? "0" : "", w = document.createElementNS("http://www.w3.org/2000/svg", "rect"), w.setAttribute("fill", h[b % h.length] + A), w.setAttribute("fill-opacity", "0.5"), w.setAttribute("width", "0"), na.push(w), Y.push(-1);
                null != x ? db(c) : alert("Your browser has no Web Audio API -> no playback.")
            }
        });
        d.tosvg("play", "%%play");
        d.tosvg("abc2svg", a);
        "" == c && (c = "no error");
        b(c)
    }

    function Q(a) {
        function l(a) {
            var b, d, c, l, f;
            a.stopPropagation();
            c = a.clientX;
            c -= this.getBoundingClientRect().left;
            l = c * Z;
            if (l < h + 24 ||
                l > e) S(); else {
                f = K.indexOf(this);
                b = (a.clientY - this.getBoundingClientRect().top) * Z;
                c = aa[f];
                for (a = 0; a < c.length; a++) if (c[a] > b) {
                    ba = a;
                    V(f);
                    break
                }
                for (a = 0; a < t.length; ++a) if (b = t[a].xy) if (c = t[a].vce, -1 != ma[ba].indexOf(c) && (d = b[0], c = b[1], b = b[3], !(d < f) && l < parseFloat(c) + b)) {
                    q = a;
                    for (l = t[a].t; t[a] && t[a].t == l;) R(t[a]), a += 1;
                    break
                }
            }
        }

        var d, c = "", k = "", g = 0;
        q = 0;
        pa = {};
        aa = [];
        var C = {}, A, f, h = 1E3, e = 0;
        ba = 0;
        a && (d = new Abc({
            imagesize: 'width="100%"', img_out: function (a) {
                -1 != a.indexOf("<svg") && (aa[g] = Object.keys(C), C = {}, g += 1, A < h && (h =
                    A), f > e && (e = f));
                c += a
            }, errmsg: function (a, b, d) {
                k += a + "\n"
            }, read_file: function (a) {
                return ""
            }, anno_start: function (a, b, c, l, h, k, B) {
                if ("note" == a || "rest" == a) l = d.ax(l).toFixed(2), h = d.ay(h).toFixed(2), B = d.ah(B), pa[b] = [g, l, h, k, B];
                "bar" == a && (h = d.ay(h), B = d.ah(B), h = Math.round(h + B), C[h] = 1, f = d.ax(l), A = d.ax(0))
            }, get_abcmodel: null
        }), d.tosvg("abc2svg", a), "" == k && (k = "no error\n"), b(k), c && (y.innerHTML = '<div id="leeg" style="height:' + qa + 'px">&nbsp;</div>', y.innerHTML += c, y.innerHTML += '<div id="leeg" style="height:' + qa + 'px">&nbsp;</div>',
            ca(document.getElementById("leeg"), "click", S), K = Array.prototype.slice.call(y.getElementsByTagName("svg")), a = Array.prototype.slice.call(y.getElementsByClassName("g")), ra = a.length ? a : K, E(), K.forEach(function (a) {
            eb && (a.style.display = "none");
            ca(a, "click", l)
        }), cb()))
    }

    function E() {
        if (0 != K.length) {
            var a, b, d, c = K[0];
            a = c.getBoundingClientRect().width;
            try {
                b = c.viewBox.baseVal.width
            } catch (k) {
                b = a
            }
            d = (d = ra[0].transform) ? d.baseVal : [];
            d = d.numberOfItems ? d.getItem(0).matrix.a : 1;
            Z = b / d / a
        }
    }

    function V(a) {
        var b = void 0 != a;
        void 0 == a && (a = Na);
        var d = P.getBoundingClientRect().top, c = K[a].getBoundingClientRect().top, k = ba,
            d = Math.round(y.scrollTop + c + (aa[a][k] - La[k]) / Z - 30 - d);
        d != y.scrollTop && (da && (y.style["scroll-behavior"] = b ? "smooth" : "auto"), y.scrollTop = d);
        Na = a
    }

    function Ha(a) {
        function b(a) {
            y.getBoundingClientRect();
            k.style.top = (c ? a.touches[0].clientY : a.clientY) - 15 + "px";
            V()
        }

        function d(a) {
            k.removeEventListener("mousemove", b);
            k.removeEventListener("touchmove", b);
            k.removeEventListener("mouseup", d);
            k.removeEventListener("touchend",
                d);
            k.removeEventListener("mouseleave", d);
            k.style.cursor = "";
            k.classList.remove("spel")
        }

        a.preventDefault();
        var c = "touchstart" == a.type, k = P;
        k.style.cursor = "row-resize";
        k.classList.add("spel");
        k.addEventListener("mousemove", b);
        k.addEventListener("touchmove", b);
        k.addEventListener("mouseup", d);
        k.addEventListener("touchend", d);
        k.addEventListener("mouseleave", d)
    }

    function R(a) {
        var b, d, c, k, g, C, A;
        C = na[a.vce];
        (b = a.xy) ? (d = b[0], c = b[1], k = b[2], g = b[3], b = b[4], a.inv && (b = g = 0), d != Y[a.vce] && ((A = C.parentNode) && A.removeChild(C),
            A = ra[d], A.insertBefore(C, A.firstChild), Y[a.vce] = d, V(d)), C.setAttribute("x", c), C.setAttribute("y", k), C.setAttribute("width", g), C.setAttribute("height", b)) : (C.setAttribute("width", 0), C.setAttribute("height", 0))
    }

    function cb() {
        var a = 0 < q ? t[q].t : 0;
        t = [];
        sa = {};
        var b = 1, d = 0, c = 0, k = 0, g = 0, C = 0, A, f;
        for (A = 0; A < M.length; ++A) {
            f = M[A];
            if (f.bt && 0 == f.v) {
                if (f.t in sa && ":" == f.bt[0]) continue;
                if (1 == b && ":" == f.bt[0] && f.t > k) {
                    A = c - 1;
                    b = 2;
                    d += f.t - k;
                    continue
                }
                2 == b && ":" == f.bt[0] && f.t > k && (b = 1);
                1 == b && ":" == f.bt[f.bt.length - 1] && (c = A, k = f.t);
                g && (f.tx || "|" != f.bt) && (g = 0, d -= f.t - C);
                2 == b && "1" == f.tx && (g = 1, C = f.t)
            }
            g || (f.bt ? sa[f.t] = 1 : t.push({t: f.t + d, xy: pa[f.ix], ns: f.ns, vce: f.v, inv: f.inv, tmp: f.tmp}))
        }
        for (q = 0; q < t.length && (f = t[q], !(f.t >= a) || f.inv); ++q) ;
        q == t.length && --q;
        R(t[q])
    }

    function Ga() {
        if (x) {
            for (var a = 1E3 * x.currentTime, b = 0, d; 0 == b;) {
                var c = t[q];
                c.tmp != N && (N = c.tmp, H.value = Math.round(N * ta));
                d = 156.25 / (c.tmp * ta);
                q == t.length - 1 ? (q = -1, b = c.ns[0].dur + 1E3) : (b = t[q + 1].t, b = (b - c.t) * d);
                c.ns.forEach(function (b, l) {
                    d = 192 >= b.dur ? 1.3 * d : 1.1 * d;
                    var g = b.mnum, k = b.dur *
                        d, f = c.vce, h = b.velo;
                    if (-1 != g) {
                        var e = g >> 7;
                        if (e in ua) {
                            var p = g % 128, g = a / 1E3, k = (k - 1) / 1E3, B, q, t, r, n, u, v = va[e][p];
                            if (v) {
                                var m = x.createBufferSource(), y = v.useflt, z = v.uselfo, E = v.useenv,
                                    F = I[f] / 127, f = (J[f] - 64) / 64;
                                m.buffer = v.buffer;
                                v.loopStart && (m.loop = !0, m.loopStart = v.loopStart, m.loopEnd = v.loopEnd);
                                m.playbackRate.value = wa[e][p];
                                z && (q = x.createOscillator(), q.frequency.value = v.lfofreq, e = x.createGain(), e.gain.value = v.lfo2vol, q.connect(e), B = x.createGain(), B.gain.value = 1, e.connect(B.gain), e = x.createGain(), e.gain.value =
                                    v.lfo2ptc, q.connect(e), e.connect(m.detune));
                                if (y) {
                                    var w = x.createBiquadFilter();
                                    w.type = "lowpass";
                                    w.frequency.value = v.filter
                                }
                                if (E) {
                                    var D = 1, e = x.createGain();
                                    e.gain.setValueAtTime(0, g);
                                    e.gain.linearRampToValueAtTime(D, g + v.envatt);
                                    p = v.envhld;
                                    n = v.envdec;
                                    k > p && (e.gain.setValueAtTime(D, g + p), k < n ? (u = k - p, n = u / (n - p) * v.envsus) : (u = n - p, n = v.envsus), D *= n, e.gain.linearRampToValueAtTime(D, g + p + u));
                                    e.gain.setValueAtTime(D, g + k);
                                    p = g + k + D * v.envrel;
                                    e.gain.linearRampToValueAtTime(0, p);
                                    t = x.createConstantSource();
                                    t.offset.value =
                                        v.env2flt;
                                    t.connect(e);
                                    e.connect(w.detune)
                                }
                                ea && (r = x.createStereoPanner(), r.pan.value = f);
                                D = h * F * v.atten * fb;
                                0 == D && (D = 1E-5);
                                h = x.createGain();
                                h.gain.setValueAtTime(1E-5, g);
                                h.gain.exponentialRampToValueAtTime(D, g + v.attack);
                                p = v.hold;
                                n = v.decay;
                                k > p && (h.gain.setValueAtTime(D, g + p), k < n ? (u = k - p, n = Math.pow(10, u / (n - p) * Math.log10(v.sustain))) : (u = n - p, n = v.sustain), D *= n, h.gain.exponentialRampToValueAtTime(D, g + p + u));
                                h.gain.setValueAtTime(D, g + k);
                                p = g + k + (100 + 20 * Math.log10(D)) / 100 * v.release;
                                h.gain.exponentialRampToValueAtTime(1E-5,
                                    p);
                                y ? (m.connect(w), w.connect(r || h)) : m.connect(r || h);
                                r && r.connect(h);
                                z ? (h.connect(B), B.connect(x.destination)) : h.connect(x.destination);
                                m.start(g);
                                z && q.start(g + v.lfodel);
                                E && t.start(g);
                                m.stop(p);
                                z && q.stop(p);
                                E && t.stop(p)
                            }
                        } else B = [144, g, h], Oa(B, a, f), B[2] = 0, Oa(B, a + k - 1, f)
                    }
                });
                R(c);
                q += 1
            }
            clearTimeout(xa);
            xa = setTimeout(Ga, b)
        } else alert("Your browser has no Web Audio API -> no playback.")
    }

    function ia(a) {
        switch (a.key) {
            case "ArrowLeft":
            case "Left":
                --q;
                R(t[q]);
                break;
            case "ArrowRight":
            case "Right":
                q += 1;
                R(t[q]);
                break;
            case "ArrowUp":
            case "Up":
                ya(1);
                break;
            case "ArrowDown":
            case "Down":
                ya(-1);
                break;
            case "m":
                $("#mbar").click();
                break;
            case " ":
                a.preventDefault && a.preventDefault(), S()
        }
    }

    function S() {
        t.length && ((Pa = 1 - Pa) ? (fa.value = "Stop", ga.style.display = "none", Ga()) : (fa.value = "Play", clearTimeout(xa)))
    }

    function ya(a) {
        /*tempScale = Math.round(tempScale / 20) * 20;
        curTemp = Math.round(curTemp / 20) * 20;*/
        a && (H.value = H.value - 0 + a);
        ta = H.value / N
    }

    function Oa(a, b, d) {
        if (0 != Qa) {
            var c = a[0] & 240, k = a[2];
            a = a[1];
            b /= 1E3;
            128 == c && Ra(a, b);
            if (144 == c) if (0 < k) {
                c = I[d] / 127;
                d = (J[d] - 64) / 64;
                var g, l = x.createBufferSource();
                l.buffer =
                    Sa[a];
                var e = x.createGain();
                e.gain.setValueAtTime(1E-5, b);
                k = k * c * .015625;
                0 == k && (k = 1E-5);
                e.gain.exponentialRampToValueAtTime(k, b + .001);
                ea && (g = x.createStereoPanner(), g.pan.value = d);
                l.connect(g || e);
                g && g.connect(e);
                e.connect(x.destination);
                l.start(b);
                za[a] = [l, e, k]
            } else Ra(a, b)
        }
    }

    function Ra(a, b) {
        var d = za[a], c = d[0], e = d[1], d = d[2];
        c && (e.gain.setValueAtTime(d, b), e.gain.exponentialRampToValueAtTime(1E-5, b + .1), c.stop(b + .1), za[a] = void 0)
    }

    function Ta(a) {
        return new Promise(function (b, d) {
            for (var c = atob(a), e = new ArrayBuffer(c.length),
                     g = new Uint8Array(e), l = 0; l < c.length; l++) g[l] = c.charCodeAt(l);
            x.decodeAudioData(e, function (a) {
                b(a)
            }, function (a) {
                d({err: 1, msg: a, data: ""})
            })
        })
    }

    function gb(a) {
        return new Promise(function (b, d) {
            function c(e) {
                var g, l, k, f;
                g = instData[e];
                l = {
                    attack: g.attack,
                    hold: g.hold,
                    decay: g.decay,
                    sustain: g.sustain,
                    release: g.release,
                    atten: g.atten,
                    filter: g.filter,
                    lfodel: g.lfodel,
                    lfofreq: g.lfofreq,
                    lfo2ptc: g.lfo2ptc,
                    lfo2vol: g.lfo2vol,
                    envatt: g.envatt,
                    envhld: g.envhld,
                    envdec: g.envdec,
                    envsus: g.envsus,
                    envrel: g.envrel,
                    env2flt: g.env2flt,
                    uselfo: Aa && .008 < g.lfofreq && (0 != g.lfo2ptc || 0 != g.lfo2vol),
                    useflt: Ba && 16E3 > g.filter,
                    useenv: Ca && 16E3 > g.filter && 0 != g.env2flt
                };
                g.loopStart && (l.loopStart = g.loopStart, l.loopEnd = g.loopEnd);
                k = g.scale;
                f = g.tune;
                for (var h = g.keyRangeLo; h <= g.keyRangeHi; h++) wa[a][h] = Math.pow(Math.pow(2, 1 / 12), (h + f) * k), va[a][h] = l, Da[128 * a + h] = 1;
                Ta(g.sample).then(function (a) {
                    l.buffer = a;
                    e < instData.length - 1 ? c(e + 1) : (instData = "", b("ok"))
                })["catch"](function (a) {
                    d(a)
                })
            }

            wa[a] = [];
            va[a] = [];
            c(1)
        })
    }

    function hb(a) {
        return new Promise(function (b,
                                     d) {
            var c = document.createElement("script");
            c.src = instUrl + "instr" + a + "mp3.js";
            c.onload = function () {
                b("ok");
                document.head.removeChild(c)
            };
            c.onerror = function (b) {
                d({err: 2, msg: "could not load " + c.src, data: a})
            };
            document.head.appendChild(c)
        })
    }

    function Ua(a) {
        void 0 == a.err && (a.err = 4, a.msg = a.toString());
        b(a.err + ", " + a.msg + ", " + a.data);
        switch (a.err) {
            case 1:
                alert("Your browser does not support decoding ogg/vorbis -> no playback");
                break;
            case 2:
                a = "Loading javascript soundfont failed, instrument " + a.data + "\nfalling back to midi-js";
                b(a);
                u.innerHTML += a.replace("\n", "<br>");
                break;
            case 3:
                b("Loading midi-js soundfont failed, instrument " + a.data + "\nmsg: " + a.msg);
                break;
            case 4:
                alert(a.msg)
        }
    }

    function ha(a) {
        function c(a) {
            return new Promise(function (c, d) {
                if (Ea[a]) c("ok"); else {
                    var e;
                    Va[a] ? e = "https://rawgit.com/gleitz/midi-js-soundfonts/gh-pages/FluidR3_GM/" + h + "-mp3" : (h = a in instTab ? instTab[a] : h, e = instUrl + h + "-mp3");
                    u.innerHTML += ", loading: " + a;
                    var f = document.createElement("script");
                    f.src = e + ".js";
                    f.onload = function () {
                        Ea[a] = MIDI.Soundfont[h];
                        Wa[e] = 1;
                        b("midi-js instr " + g + " geladen");
                        c("ok")
                    };
                    f.onerror = function () {
                        d({err: 3, msg: "could not load:" + e, data: a})
                    };
                    e in Wa ? c("ok") : document.head.appendChild(f)
                }
            })
        }

        function d(b) {
            c(b).then(function () {
                var a = Ea[b][q + f].split(",")[1];
                return Ta(a)
            }).then(function (c) {
                Sa[k] = c;
                u.innerHTML += ", " + b + ":" + n;
                Da[k] = 1;
                ha(a)
            })["catch"](function (a) {
                Ua(a);
                -1 == a.msg.indexOf("gleitz") ? (Va[b] = 1, u.innerHTML += "<br>loading local midi-js font failed, instrument: " + a.data + "<br>trying Github ...", d(b)) : (alert("loading soundfont from Github failed, giving up."),
                    u.style.display = "none")
            })
        }

        var e = "C Db D Eb E F Gb G Ab A Bb B".split(" "), k = a.shift();
        if (k) {
            var g = k >> 7, n = k % 128, q = e[n % 12], f = Math.floor(n / 12) - 1, h = ib[g];
            ua[g] ? ha(a) : T && !Xa[g] ? hb(g).then(function () {
                u.innerHTML += "loading instrument " + g + "<br>";
                return gb(g)
            }).then(function () {
                b("instr " + g + " geladen");
                ua[g] = 1;
                ha(a)
            })["catch"](function (a) {
                Ua(a);
                Xa[g] = 1;
                d(g)
            }) : d(g)
        } else Qa = 1, u.style.display = "none"
    }

    function db(a) {
        a = Object.keys(a).filter(function (a) {
            return !(a in Da)
        });
        a.length && (u.innerHTML = "", u.style.display =
            "block", ha(a))
    }

    function jb() {
        var a, c = "", d = "", n, k, g, q, r, f, h = {};
        G.innerHTML = "";
        a = window.location.href.replace("?dl=0", "").split("?");
        if (1 < a.length) for (a = a[1].split("&"), r = 0; r < a.length; r++) h = a[r].replace(/d:(\w{15}\/[^.]+\.)/, "https://dl.dropboxusercontent.com/s/$1"), "noErr" == h ? g = 1 : "noMenu" == h ? n = 1 : "noSave" == h ? k = 1 : "noErr" == h ? g = 1 : "noDash" == h ? q = 1 : "noRT" == h ? T = 0 : "noPF" == h ? Ia = 1 : "noLB" == h ? Ja = 1 : (f = h.match(/noCur=([\da-fA-F]{2})/)) ? oa = parseInt(f[1], 16) : "nosm" == h ? da = !1 : d = h;
        h = (f = document.getElementById("parms")) ?
            JSON.parse(f.innerHTML) : {};
        void 0 != h.topSpace && (qa = h.topSpace);
        void 0 != h.notationHeight && (Ya = h.notationHeight);
        void 0 != h.fileURL && (d = h.fileURL);
        void 0 != h.gTempo && (O = h.gTempo);
        void 0 != h.noCur && (oa = h.noCur);
        if (void 0 != h.noDash && h.noDash || q) P.style.visibility = "hidden";
        if (void 0 != h.noSave && h.noSave || k) document.getElementById("savlbl").style.display = "none";
        if (void 0 != h.noMenu && h.noMenu || n) document.getElementById("mbar").style.display = "none";
        if (void 0 != h.noErr && h.noErr || g) G.style.display = "none", G.style.height =
            "0px";
        /(\.xml$)|(\.abc$)/.test(d) && (c = d, d = "");
        if (c) {
            var t = document.getElementById("wait");
            t.style.display = "block";
            var p = new XMLHttpRequest;
            p.open("GET", c, !0);
            p.onload = function () {
                b("preload ok");
                e(p.response);
                ga.style.display = "block";
                t.style.display = "none"
            };
            p.onerror = function () {
                t.innerHTML += "\npreload failed"
            };
            p.send()
        }
    }

    function Za() {
        var a = G.clientHeight, a = Math.round(100 * a / document.documentElement.clientHeight);
        y.style.height = Ya - a + "%"
    }

    function kb() {
        var a = "", c = "";
        if (la && ((new Abc({
            imagesize: 'width="100%"',
            img_out: function (b) {
                a += b
            }, errmsg: function (a, b, d) {
                c += a + "\n"
            }, read_file: null, anno_start: null, get_abcmodel: null
        })).tosvg("abc2svg", la), "" == c && (c = "no error\n"), b(c), a)) {
            var d, e, k = '<html><meta charset="utf-8"><div>\n' + a + "\n</div></html>\n";
            e = ka + ".html";
            try {
                d = document.createElement("a"), d.href = "data:text/plain;charset=utf-8," + encodeURIComponent(k), d.download = e, d.text = "Save ABC file", document.getElementById("saveDiv").appendChild(d), d.click()
            } catch (g) {
                confirm("Do you want to save the ABC text?") && (document.open("text/html"),
                    document.write("<pre>" + k + "</pre>"), document.close())
            }
        }
    }

    function ca(a, b, c) {
        function d(b) {
            a.removeEventListener("mousedown", d);
            a.removeEventListener("touchend", d);
            console.log("event listeners removed from " + a.nodeName + "#" + a.id);
            x && "suspended" == x.state && x.resume().then(function () {
                console.log("resuming audioContext")
            })
        }

        a.addEventListener("mousedown", d);
        a.addEventListener("touchend", d);
        a.addEventListener(b, c)
    }

    function $a() {
        function a(a) {
            L.checked = !a;
            L.disabled = a;
            ab.style.color = a ? "#aaa" : "#000"
        }

        if ("undefined" ==
            typeof Dropbox) {
            a(!0);
            var e = document.createElement("script");
            e.src = "https://www.dropbox.com/static/api/2/dropins.js";
            e.onload = function () {
                a(!1);
                Dropbox.init({appKey: "ckknarypgq10318"});
                var b = Dropbox.createChooseButton({
                    success: c, cancel: function () {
                    }, linkType: "direct", multiselect: !1, extensions: [".xml", ".abc"]
                });
                bb.append(b);
                $a()
            };
            e.onerror = function () {
                b("loading dropbox API failed")
            };
            document.head.appendChild(e)
        } else document.querySelector(".dropbox-dropin-btn").style.display = L.checked ? "inline-block" :
            "none", W.style.display = L.checked ? "none" : "inline-block"
    }

    function lb() {
        var a = document.body, b = a.requestFullscreen || a.mozRequestFullScreen || a.webkitRequestFullscreen,
            c = document.exitFullscreen || document.mozCancelFullScreen || document.webkitExitFullscreen;
        b && c && ($("#fscr").prop("checked") ? b.call(a) : c.call(document))
    }

    var la, M, ma, Ma, ka, q = 0, Pa = 0, xa, Qa = 0, da, t = [], X = [], sa = {}, pa = {}, aa = [], K = [], ra = [],
        eb, qa = 500, Z, ba = 0, Y = [], Na = 0, na = [], x = null, Sa = [], za = [], Da = {}, Ya = 100, ja = null,
        La = [], Xa = {}, Va = {}, ua = {}, Ea = [], Ka = {}, I = [],
        J = [], y = null, u, G, bb, P, W, H, L, ab, Fa, U, ga, fa,
        ib = "acoustic_grand_piano bright_acoustic_piano electric_grand_piano honkytonk_piano electric_piano_1 electric_piano_2 harpsichord clavinet celesta glockenspiel music_box vibraphone marimba xylophone tubular_bells dulcimer drawbar_organ percussive_organ rock_organ church_organ reed_organ accordion harmonica tango_accordion acoustic_guitar_nylon acoustic_guitar_steel electric_guitar_jazz electric_guitar_clean electric_guitar_muted overdriven_guitar distortion_guitar guitar_harmonics acoustic_bass electric_bass_finger electric_bass_pick fretless_bass slap_bass_1 slap_bass_2 synth_bass_1 synth_bass_2 violin viola cello contrabass tremolo_strings pizzicato_strings orchestral_harp timpani string_ensemble_1 string_ensemble_2 synth_strings_1 synth_strings_2 choir_aahs voice_oohs synth_choir orchestra_hit trumpet trombone tuba muted_trumpet french_horn brass_section synth_brass_1 synth_brass_2 soprano_sax alto_sax tenor_sax baritone_sax oboe english_horn bassoon clarinet piccolo flute recorder pan_flute blown_bottle shakuhachi whistle ocarina lead_1_square lead_2_sawtooth lead_3_calliope lead_4_chiff lead_5_charang lead_6_voice lead_7_fifths lead_8_bass__lead pad_1_new_age pad_2_warm pad_3_polysynth pad_4_choir pad_5_bowed pad_6_metallic pad_7_halo pad_8_sweep fx_1_rain fx_2_soundtrack fx_3_crystal fx_4_atmosphere fx_5_brightness fx_6_goblins fx_7_echoes fx_8_scifi sitar banjo shamisen koto kalimba bagpipe fiddle shanai tinkle_bell agogo steel_drums woodblock taiko_drum melodic_tom synth_drum reverse_cymbal guitar_fret_noise breath_noise seashore bird_tweet telephone_ring helicopter applause gunshot".split(" "),
        Wa = {}, O = 120, N = 120, ta = 1, va = [], wa = [], T = 1, Ia = 0, Ja = 0, oa = 0, fb = .5 / 60, ea = 1,
        Aa = 1, Ba = 1, Ca = 1;
    document.addEventListener("DOMContentLoaded", function () {
        u = document.getElementById("comp");
        y = document.getElementById("notation");
        G = document.getElementById("err");
        P = document.getElementById("rollijn");
        bb = document.getElementById("abcfile");
        W = document.getElementById("fknp");
        H = document.getElementById("tempo");
        ga = document.getElementById("playbk");
        fa = document.getElementById("play");
        document.getElementById("kwart").appendChild(document.getElementById("mtrsvg"));
        ca(fa, "click", S);
        ca(ga, "click", S);
        W.addEventListener("change", r);
        document.getElementById("save").addEventListener("click", kb);
        P.addEventListener("mousedown", Ha);
        P.addEventListener("touchstart", Ha);
        H.addEventListener("change", ya);
        window.addEventListener("resize", function () {
            E();
            Za();
            V()
        });
        y.addEventListener("drop", z);
        y.addEventListener("dragover", function (a) {
            a.stopPropagation();
            a.preventDefault();
            a.dataTransfer.dropEffect = "copy"
        });
        y.addEventListener("dragenter", function () {
            this.classList.add("indrag")
        });
        y.addEventListener("dragleave", function () {
            this.classList.remove("indrag")
        });
        L = document.getElementById("drpuse");
        L.checked = !1;
        L.addEventListener("click", $a);
        ab = document.getElementById("drplbl");
        Fa = document.getElementById("mbar");
        U = document.getElementById("menu");
        U.style.display = "none";
        Fa.addEventListener("click", function (a) {
            a.stopPropagation();
            a = "none" == U.style.display;
            U.style.display = a ? "block" : "none";
            Fa.style.background = a ? "#aaa" : ""
        });
        U.addEventListener("click", function (a) {
            a.stopPropagation()
        });
        da =
            CSS.supports("scroll-behavior", "smooth");
        jb();
        Za();
        var a = window.AudioContext || window.webkitAudioContext;
        if (x = void 0 != a ? new a : null) {
            x.createStereoPanner || (ea = 0);
            x.createOscillator || (Aa = 0);
            x.createBiquadFilter || (Ba = 0);
            x.createConstantSource || (Ca = 0);
            var a = [], b = 0;
            ea || a.push("Your browser does not support the StereoPanner element");
            T && !Aa && (a.push("Your browser does not support the Oscillator element"), b = 1);
            T && !Ba && (a.push("Your browser does not support the BiquadFilter element"), b = 1);
            T && !Ca && (a.push("Your browser does not support the ConstantSource element"),
                b = 1);
            a.length && alert(a.join("\n") + (b ? "\nThe instrument(s) will sound (very) wrong" : ""))
        } else alert("Your browser has no Web Audio API -> no playback.");
        da || alert("Your browser does not support smooth scrolling");
        document.getElementById("verlab").innerHTML = "<span>Version:</span>" + xmlplay_VERSION;
        $("#fscr").on("change", lb);
        $("body").on("fullscreenchange webkitfullscreenchange mozfullscreenchange", function () {
            var a = document.fullscreenElement || document.webkitFullscreenElement || document.mozFullScreenElement;
            $("#fscr").prop("checked", null != a)
        });
        document.body.addEventListener("keydown", ia)
    })
})();
