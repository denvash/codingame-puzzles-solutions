// https://www.codingame.com/training/expert/nintendo-sponsored-contest

#include <iostream>
#include <iomanip>
#include <sstream>
#include <vector>
#include <algorithm>
#include <bitset>
#include <assert.h>

using namespace std;

#define RAND_RANGE(min, max) { rand() % (max-min + 1) + min }
#define APPEND(a, b) { a.insert(a.end(), b.begin(), b.end()); }
#define REV(v) { reverse(v.begin(), v.end()); }

 class Polynomial;
 class Factors;


class Polynomial {
public:
    vector<bool> bits;

    Polynomial() : bits(vector<bool>({0})) {}

    Polynomial(unsigned long size) : bits(vector<bool>(size)) {}

    Polynomial(vector<bool> bits) : bits(bits) {
        if (bits.size() == 0)
            this->bits.push_back(0);
    }

    Polynomial(string bitstring) {
        for (auto c : bitstring)
            bits.push_back(c == '1');
    }

    // GF operations
    Polynomial operator<<(int offset) {
        rotate(bits.begin(), bits.begin() + offset, bits.end());
        for (unsigned long i = 0; i < offset; i++)
            bits[bits.size() - 1 - i] = false;
        monic();
        return *this;
    }

    Polynomial operator>>(int offset) {
        REV(bits);
        *this << offset;
        REV(bits);
        monic();
        return *this;
    }

    Polynomial operator+=(Polynomial* other) {
        Polynomial::bring_to_same_size(this, other);
        unsigned long size = this->bits.size();
                for (int i = 0; i < size; i++)
            this->bits[i] = this->bits[i] != other->bits[i];
        return this->monic();
    }

    friend Polynomial operator+(const Polynomial &_a, const Polynomial &_b) {
        Polynomial a = _a.copy(), b = _b.copy();
        Polynomial::bring_to_same_size(&a, &b);
        unsigned long size = a.bits.size();
        for (int i = 0; i < size; i++)
            a.bits[i] = a.bits[i] != b.bits[i];
        return a.monic();
    }

    friend Polynomial operator*(const Polynomial &_a, const Polynomial &_b) {
        Polynomial a = _a.copy(), b = _b.copy();
        Polynomial::bring_to_same_size(&a, &b);
        unsigned long size = a.size();

        Polynomial result(size * 2);
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                result.bits[i + j + 1] = result.bits[i + j + 1] != (a.bits[i] && b.bits[j]);

        return result.monic();
    }

    friend Polynomial operator^(const Polynomial &p, int exponent) {
        Polynomial ret = p.copy();
        if (exponent == 0)
            return Polynomial::ones(p.degree());
        for (int i = 1; i < exponent; i++)
            ret = ret * ret;
        return ret.monic();
    }

    Polynomial square() const {
        auto accu = vector<bool>();
        for (auto b : bits) {
            accu.push_back(0);
            accu.push_back(b);
        }
        return Polynomial(accu).monic();
    }

    friend pair<Polynomial, Polynomial> operator/(const Polynomial &_a, const Polynomial &b) {
        if (_a.degree() < b.degree())
            return b / _a;
        Polynomial a = _a.copy();
        unsigned long m = a.degree(), n = b.degree();
        vector<bool> u = a.bits, v = b.bits, q(m - n + 1);
        auto monic = v[0];
        for (unsigned long k = 0; k < m - n + 1; k++) {
            q[k] = u[k] && monic;
            if (!q[k]) continue;
            for (unsigned long j = k; j < k + n + 1; j++)
                u[j] = (u[j] != (q[k] && v[j - k]));
        }
        vector<bool> r(n);
        for (unsigned long i = 0; i < r.size(); i++)
            r[i] = u[m - r.size() + 1 + i];

        return pair<Polynomial, Polynomial>(Polynomial(q).monic(), Polynomial(r).monic());
    }

    friend Polynomial operator|(const Polynomial &a, const Polynomial &b) {
        return (a / b).first;
    }

    friend Polynomial operator%(const Polynomial &a, const Polynomial &b) {
        return (a / b).second;
    }

    friend Polynomial gcd(const Polynomial &a, const Polynomial &b) {
        if (a.degree() < b.degree())
            return gcd(b, a);
                        if (b.is_zero()) return a;
        return gcd(b, a % b);
    }

    // Utilities
    unsigned long degree() const {
        for (unsigned long i = 0; i < bits.size(); i++)
            if (bits[i]) return bits.size() - 1 - i;
        return 0;
    }

    friend bool operator==(const Polynomial &a, string s) {
        return a._b() == s;
    }

    bool is_zero() const {
        for (auto b : bits)
            if (b) return false;
        return true;
    }

    bool is_one() const {
        if (!bits[bits.size() - 1])
            return false;
        for (unsigned long i = 0; i < bits.size() - 1; i++)
            if (bits[i]) return false;
        return true;
    }

    bool is_monic() const {
        return bits[0] == 1 || bits.size() == 1;
    }

    Polynomial copy() const {
        Polynomial ret(bits.size());
        std::copy(bits.begin(), bits.end(), ret.bits.begin());
        return ret;
    }

    string _b() const {
        string ret = "";
        for (auto b : bits)
            ret += b ? '1':'0';
        return ret;
    }

    template<const unsigned num, const char separator>
    static void separate(string & input) {
        for (auto it = input.begin(); (num+1) <= distance(it, input.end()); ++it) {
            advance(it,num);
            it = input.insert(it,separator);
        }
    }

    string _bW(unsigned long size) const {
        string ret = "";
        for (auto b : bits)
            ret += b ? '1':'0';
        unsigned long fill = size - ret.size();
        ret = string(fill, '0').append(ret);
        separate<32, ' '>(ret);
        return ret;
    }

    string _hex(unsigned long size) const {
        ostringstream res;
        istringstream stream1(_bW(size));
        vector<bitset<32>> v;
        for (int i = 0; i < size/32; i++) {
            string tmp;
            stream1 >> tmp;
            v.insert(v.begin(), bitset<32>(tmp));
        }
        for (auto s : v) {
            if (s != *v.begin())
                res << " ";
            res << setfill('0') << setw(8) << hex << s.to_ulong();
        }
        return res.str();
    }

    string to_expr() const {
        string expr = "";
        unsigned long exponent = bits.size() - 1;
        for (auto b : bits) {
            if (exponent == 0)
                expr += b ? "1":"0";
            else
                expr += (b ? ("x^" + to_string(exponent) + " + ") : "");
            exponent--;
        }
        return expr;
    }

    Polynomial monic() {
        bits.erase(bits.begin(), find(bits.begin(), bits.end(), 1));
        if (bits.empty())
            bits.push_back(0);
                return *this;
    }

    static Polynomial ones(unsigned long n) {
        string accu = "";
        for (int i = 0; i < n; i++)
            accu += '1';
        return Polynomial(accu);
    }

    static void bring_to_same_size(Polynomial *a, Polynomial *b) {
        if (a->size() < b->size())
            bring_to_same_size(b, a);
        else if (a->size() > b->size()) {
            unsigned long offset = a->size() - b->size();
            REV(b->bits);
            for (unsigned long i = 0; i < offset; i++)
                b->bits.push_back(0);
            REV(b->bits);
        }
    }

    static Polynomial generate_random(int min_degree, int max_degree) {
        // Random degree
        int degree = RAND_RANGE(min_degree, max_degree);
        // Random bits
        vector<bool> bits;
        bits.push_back(1); // always monic
        for (int i = 0; i < degree; i++)
            bits.push_back((bool) (rand() % 2));
        return Polynomial(bits).monic();
    }

    unsigned long size() const { return bits.size(); }
};

class Factors {
public:
    vector<Polynomial> factors;
    Factors() : factors() {}
    Factors(unsigned long size) : factors(vector<Polynomial>(size, Polynomial("1"))) {

    }
    Factors(const Polynomial &p) : factors({p}) {}
    Factors(const vector<Polynomial> &factors) : factors(factors) {}

    Factors operator*=(Polynomial p) {
        factors.push_back(p);
        return *this;
    }

    Factors operator+(Factors other) {
        APPEND(this->factors, other.factors)
        return *this;
    }

    Polynomial& operator[](const int index) {
        return factors[index - 1];
    }

    unsigned long size() {
        return factors.size();
    }

    string to_string() {
        string ret = "";
        for (auto f : factors)
            ret += f._b() + " ";
        return ret;
    }
};

Factors DDF(Polynomial f) {
    Factors result = {};
    Polynomial fs = f.copy();
    int i = 1;
    while (fs.degree() >= 2*i) {
        Polynomial rotx = vector<bool>((unsigned long) ((1 << i) - 1), 0);
        rotx.bits[0] = 1;
        if (rotx.bits[1])
            rotx.bits[1] = 0;
        Polynomial g = gcd(fs, rotx % fs);
        if (!g.is_one()) {
            cout << "I: " << i << endl;
            result *= g;
            auto div = fs / g;
            assert(div.second == "0");
            fs = div.first;
        }
        i++;
    }
    if (result.factors.empty())
        result *= f;
    return result;
}

Factors EDF(Polynomial A, int d) {
    if (A.degree() == d)
        return {A};
    for (;;) {
        Polynomial T = Polynomial::generate_random(1, 2 * d + 1);
        Polynomial W = T;
        for (int i = 0; i < d -1; i++) {
            T = T.square() % A;
            W += &T;
        }
        Polynomial U = gcd(A, W);
        if (U.degree() > 0 && U.degree() < A.degree()) {
            Factors f1 = EDF(U, d);
            Factors f2 = EDF(A | U, d);
            return  f1 + f2;
        }
    }
}


int main() {
    srand(time(NULL));

    int size;
    cin >> size;
    string encrypted;


    for (int i = 0; i < size/16; i++) {
        uint32_t inp;
        cin >> hex >> inp;
        string bin = bitset<32>(inp).to_string();
        encrypted = bin + encrypted;
    }

    Polynomial poly = Polynomial(encrypted);
    poly.monic();
    Factors solution = EDF(poly, poly.degree()/2);
//    Factors solution = DDF(poly);

    vector<bitset<32>> res1(size/32), res2(size/32);
    istringstream stream1(solution.factors[0]._bW(size));
    istringstream stream2(solution.factors[1]._bW(size));

    for (int i = size/32 - 1; i >= 0; i--) {
        string tmp;
        stream1 >> tmp;
        res1[i] = bitset<32>(tmp);
        stream2 >> tmp;
        res2[i] = bitset<32>(tmp);
    }

    ostringstream out1, out2;
    for (auto res1_bit : res1) {
        if (res1_bit != *res1.begin())
            out1 << " ";
        out1 << setfill('0') << setw(8) << hex << res1_bit.to_ulong();
    }
    for (auto &res2_bit : res2) {
        if (res2_bit != *res2.begin())
            out2 << " ";
        out2 << setfill('0') << setw(8) << hex << res2_bit.to_ulong();
    }

    if (poly.degree() < size)
        cout << Polynomial("1")._hex(size) << " " << poly._hex(size) << endl;

    if (out1.str() == out2.str())
        cout << out1.str() << " " << out2.str() << "\n";
    else if (out1.str() < out2.str())
        cout << out1.str() << " " << out2.str() << "\n" << out2.str() << " " << out1.str() << "\n";
    else
        cout << out2.str() << " " << out1.str() << "\n" << out1.str() << " " << out2.str() << "\n";

    if (poly.degree() < size)
        cout << poly._hex(size) << " " << Polynomial("1")._hex(size) << endl;

    return 0;
}