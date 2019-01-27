#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <set>

using namespace std;

int main()
{
    int N;
    cin >> N; cin.ignore();

    vector<string> subseqs(N);
    vector<int> order(N);

    for (int i = 0; i < N; i++) {
        string subseq;
        cin >> subseq; cin.ignore();

        subseqs[i] = subseq;
        order[i] = i;
    }

    string bestWord;
    string lastWord;
    do
    {
        bool match = false;
        string lastWord = subseqs[order[0]];
        for (unsigned int i = 1; i < subseqs.size(); ++i)
        {
            match = false;
            string first = lastWord;
            string second = subseqs[order[i]];

            for (unsigned int j = 0; j < first.size(); ++j)
            {
                if (second.size() >= first.size() - j)
                {
                    match = equal(first.begin() + j,
                                  first.end(),
                                  second.begin());

                    if (match)
                    {
                        lastWord = first + second.substr(first.size() - j);
                        break;
                    }
                }
                else
                {
                    match = equal(first.begin() + j,
                                  first.begin() + j + second.size(),
                                  second.begin());

                    if (match)
                    {
                        lastWord = first;
                        break;
                    }
                }
            }

            // If no overlap, place the sequences begind each other
            if (!match)
                lastWord = first + second;
        }

        if (bestWord.empty() || (lastWord.size() < bestWord.size()))
            bestWord = lastWord;
    }
    while (next_permutation(order.begin(), order.end()));

    cout << bestWord.size() << endl;
}